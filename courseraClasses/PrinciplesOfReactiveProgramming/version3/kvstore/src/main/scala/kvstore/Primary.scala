package kvstore

import akka.actor.{ActorRef, Cancellable}
import akka.event.LoggingReceive
import kvstore.Arbiter.Replicas


import scala.collection.immutable.Set

import akka.actor.Actor.Receive
import kvstore.Replica._


import scala.concurrent.duration._
import scala.language.postfixOps


trait Primary {
  this: Replica =>

  import Replica._
  import Replicator._
  import Persistence._
  import context.dispatcher

  var primaryPersistingAcks = Map.empty[Long, (ActorRef, Cancellable)]

  var counter = 0


  /* TODO Behavior for  the leader role. */
  val leader: Receive = LoggingReceive {
    case Insert(key, value, id) =>
      insert(key, value, id)
    case Remove(key, id) =>
      remove(key, id)
    case Get(key, id) =>
      get(key, id)
    //From Arbiter
    case Replicas(replicas) =>
      arbiterInforms(replicas)
    //From Replicator
    case Replicated(key, id) =>
      replicatorReplicated(key, id)
    //From Persistor
    case Persisted(key, id) =>
      persistConfirm(key, id)
    case _ => log.info("Got message in primary replica")

  }

  def insert(key: String, value: String, id: Long) : Unit = {
    cache += (key -> value)

    if (!secondaryReplicatorRefSet.isEmpty) {
      replicationAcks += (id -> (sender, secondaryReplicatorRefSet.size))
      secondaryReplicatorRefSet foreach { replicatorRef =>
        replicatorAcks.addBinding(replicatorRef, id)
        replicatorRef ! Replicate(key, Some(value), id)
      }
    }

    val cancellable: Cancellable = context.system.scheduler.schedule(0 milliseconds, 100 milliseconds, persistor, Persist(key, Some(value), id))
    primaryPersistingAcks += (id -> (sender /* ClientRef */, cancellable))

    context.system.scheduler.scheduleOnce(1 second) {
      primaryPersistingAcks get id match {
        case Some((s, cancellable)) => {
          cancellable.cancel
          primaryPersistingAcks -= id
          s ! OperationFailed(id)
        }
        case None => {
          replicationAcks get id match {
            case Some((s, c)) => {
              replicationAcks -= id
              s ! OperationFailed(id)
            }
            case None =>
          }
        }
      }
    }
  }

  def remove(key: String, id: Long) : Unit = {
    cache -= key

    if (!secondaryReplicatorRefSet.isEmpty) {
      replicationAcks += id -> (sender, secondaryReplicatorRefSet.size)
      secondaryReplicatorRefSet foreach { replicatorRef =>
        replicatorAcks.addBinding(replicatorRef, id)
        replicatorRef ! Replicate(key, None, id)
      }
    }

    val cancellable : Cancellable = context.system.scheduler.schedule(0 milliseconds, 100 milliseconds, persistor, Persist(key, None, id))
    primaryPersistingAcks += id -> (sender /* Client Ref */, cancellable)

    context.system.scheduler.scheduleOnce(1 second) {
      primaryPersistingAcks get id match {
        case Some((clientRef, cancellable)) => {
          cancellable.cancel
          primaryPersistingAcks -= id
          clientRef ! OperationFailed(id)
        }
        case None => {
          replicationAcks get id match {
            case Some((clientRef, cancellable)) => {
              replicationAcks -= id
              clientRef ! OperationFailed(id)
            }
            case None =>
          }
        }
      }
    }
  }

  def get(key: String, id: Long) : Unit =  {
    val value: Option[String] = cache get key
    sender ! GetResult(key, value, id)
  }

  def arbiterInforms(replicas: Set[ActorRef]) : Unit = {

    val secondaryActorRefs = replicas - self
    assert(secondaryActorRefs.size == (replicas.size -1))
    val joinedSet = secondaryActorRefs -- secondaryReplicaToReplicatorMap.keySet
    log.info("Numbers of joiners: {}", joinedSet.size)
    val leftSet =  secondaryReplicaToReplicatorMap.keySet -- secondaryActorRefs
    log.info("Numbers of leavers: {}", leftSet.size)

    joinedSet foreach {
      newSecondaryReplicaRef =>
        val secondaryReplicatorRef = context.system.actorOf(Replicator.props(newSecondaryReplicaRef))
        context.watch(secondaryReplicatorRef)
        secondaryReplicaToReplicatorMap += newSecondaryReplicaRef -> secondaryReplicatorRef
        secondaryReplicatorRefSet += secondaryReplicatorRef
        cache foreach { kvTuple =>
          secondaryReplicatorRef ! Replicate(kvTuple._1, Some(kvTuple._2), counter)
          counter += 1
        }
    }

    leftSet foreach { leftActorRef =>
      secondaryReplicaToReplicatorMap get leftActorRef match {
        case Some(replicatorRef) => {
          context.stop(replicatorRef)
          secondaryReplicaToReplicatorMap -= leftActorRef
          secondaryReplicatorRefSet -= replicatorRef

          replicatorAcks get replicatorRef match {
            case Some(outstandingAcks) => {
              outstandingAcks foreach { a =>
                self ! Replicated("", a)
              }
              replicatorAcks -= replicatorRef
            }
            case None =>
          }
        }
        case None =>
      }
    }
  }

  def replicatorReplicated(key: String, id: Long) :Unit = {
    replicatorAcks get sender match {
      case Some(boundIdSet) => {
        boundIdSet -= id
      }
      case None =>
    }
    replicationAcks get id match {
      case Some((clientRef, replicatorRefCount)) => {
        val newValue = replicatorRefCount - 1
        if (newValue == 0) {
          replicationAcks -= id
          if (!(primaryPersistingAcks contains id)) {
            clientRef ! OperationAck(id)
          }
        } else {
          replicationAcks += id -> (clientRef, newValue)
        }
      }
      case None =>
    }
  }

  def persistConfirm(key: String, id: Long) : Unit = {
    primaryPersistingAcks get id match {
      case Some((clientRef, cancellable)) => {
        cancellable.cancel
        primaryPersistingAcks -= id
        if (!(replicationAcks contains id)) {
          clientRef ! OperationAck(id)
        }
      }
      case None =>
    }
  }
}
