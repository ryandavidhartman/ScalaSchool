package kvstore

import akka.actor._
import kvstore.Arbiter._
import Persistence._
import Replica._
import Replicator._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

object Replica {
  sealed trait Operation {
    def key: String
    def id: Long
  }
  case class Insert(key: String, value: String, id: Long) extends Operation
  case class Remove(key: String, id: Long) extends Operation
  case class Get(key: String, id: Long) extends Operation

  sealed trait OperationReply
  case class OperationAck(id: Long) extends OperationReply
  case class OperationFailed(id: Long) extends OperationReply
  case class GetResult(key: String, valueOption: Option[String], id: Long) extends OperationReply

  def props(arbiter: ActorRef, persistenceProps: Props): Props = Props(new Replica(arbiter, persistenceProps))
}

class Replica(val arbiter: ActorRef, persistenceProps: Props) extends Actor {


  arbiter ! Join

  /*
   * The contents of this actor is just a suggestion, you can implement it in any way you like.
   */

  var kv = Map.empty[String, String]
  // a map from secondary replicas to replicators
  var secondaries = Map.empty[ActorRef, ActorRef]
  // the current set of replicators
  var replicators = Set.empty[ActorRef]
  // map from sequence number to tuple of sender, id, and persist
  var persists = Map.empty[Long, (ActorRef, Long, Persist)]
  // map from sequence number to tuple of sender, id, and set of not replicated replicators
  var replications = Map.empty[Long, (ActorRef, Long, Set[ActorRef])]

  var persistActor = context.actorOf(persistenceProps)
  context.watch(persistActor)

  context.system.scheduler.schedule(0.millisecond, 100.milliseconds) {
    persists foreach { persistActor ! _._2._3 }
  }

  var _seqCounter = 0L
  def nextSeq = {
    val ret = _seqCounter
    _seqCounter += 1
    ret
  }


  def receive = {
    case JoinedPrimary   => context.become(leader)
    case JoinedSecondary => context.become(replica(0))
  }

  def undoIfFailed(seq: Long, key: String, oldValue: Option[String], self: ActorRef): Unit = {
    var succeed = true
    if (persists.contains(seq)) {
      val (sender, id, _) = persists(seq)
      if (id >= 0) sender ! OperationFailed(id)
      persists -= seq
      succeed = false
    } else if (replications.contains(seq)) {
      val (sender, id, _) = replications(seq)
      if (id >= 0) sender ! OperationFailed(id)
      replications -= seq
      succeed = false
    }
    if (!succeed) {
      oldValue match {
        case Some(value) => self ! Insert(key, value, -seq-1)
        case None => self ! Remove(key, -seq-1)
      }
    }
  }

  val leader: Receive = {
    case Insert(key, value, id) =>
      val oldValue = kv.get(key)
      kv += key -> value
      val seq = if (id >= 0) nextSeq else id
      val persist = Persist(key, Some(value), seq)
      persists += seq -> Tuple3(sender(), id, persist)
      persistActor ! persist
      if (replicators.nonEmpty) {
        replications += seq -> Tuple3(sender(), id, replicators)
      }
      replicators foreach { _ ! Replicate(key, Some(value), seq) }
      context.system.scheduler.scheduleOnce(1.second) {
        undoIfFailed(seq, key, oldValue, self)
      }

    case Remove(key, id) =>
      val oldValue = kv.get(key)
      kv -= key
      val seq = if (id >= 0) nextSeq else id
      val persist = Persist(key, None, seq)
      persists += seq -> Tuple3(sender(), id, persist)
      persistActor ! persist
      if (replicators.nonEmpty) {
        replications += seq -> Tuple3(sender(), id, replicators)
      }
      replicators foreach { _ ! Replicate(key, None, seq) }
      context.system.scheduler.scheduleOnce(1.second) {
        undoIfFailed(seq, key, oldValue, self)
      }

    case Get(key, id) =>
      sender() ! GetResult(key, kv.get(key), id)

    case Replicas(replicas) =>
      replications foreach {
        case (seq, (sender, id, _)) =>
          if (!persists.contains(seq)) {
            sender ! OperationAck(id)
          }
      }
      replications = Map.empty[Long, (ActorRef, Long, Set[ActorRef])]
      val oldReplicas = secondaries.keys.toSeq.toSet
      oldReplicas -- replicas - self foreach { replica =>
        val replicator = secondaries(replica)
        secondaries -= replica
        replicators -= replicator
        context.stop(replicator)
      }

      val newReplicas = replicas -- oldReplicas - self
      newReplicas foreach { replica =>
        val replicator = context.actorOf(Replicator.props(replica))
        secondaries += replica -> replicator
        replicators += replicator
      }

      kv.zipWithIndex foreach {
        case ((key, value), i) =>
          val rep = Replicate(key, Some(value), -i-1)
          newReplicas.map(secondaries) foreach { _ ! rep }
      }

    case Replicated(_, seq) =>
      if (seq >= 0) {
        val (sender, id, reps) = replications(seq)
        replications += seq -> Tuple3(sender, id, reps - context.sender())
        if (replications(seq)._3.isEmpty) {
          replications -= seq
          if (!persists.contains(seq)) {
            sender ! OperationAck(id)
          }
        }
      }

    case Persisted(_, seq) =>
      val (sender, id, _) = persists(seq)
      persists -= seq
      if (!replications.contains(seq)) {
        sender ! OperationAck(id)
      }

    case Terminated(actor) =>
      if (actor == persistActor) {
        persistActor = context.actorOf(persistenceProps)
        context.watch(persistActor)
        persists foreach { persistActor ! _._2._3 }
      }
  }

  def replica(expectedId: Long): Receive = {
    case Get(key, id) =>
      sender() ! GetResult(key, kv.get(key), id)

    case Snapshot(key, valueOption, id) =>
      if (id <= expectedId) {
        if (id == expectedId) {
          valueOption match {
            case Some(value) =>
              kv += key -> value

            case None =>
              kv -= key
          }

          val seq = nextSeq
          val persist = Persist(key, valueOption, seq)
          persistActor ! persist
          persists += seq -> Tuple3(sender(), id, persist)
        } else {
          sender() ! SnapshotAck(key, id)
        }
        context.become(replica(math.max(expectedId, id + 1)))
      }

    case Persisted(key, seq) =>
      val (sender, id, Persist(key, _, _)) = persists(seq)
      persists -= seq
      sender ! SnapshotAck(key, id)

    case Terminated(actor) =>
      if (actor == persistActor) {
        persistActor = context.actorOf(persistenceProps)
        context.watch(persistActor)
        persists foreach { persistActor ! _._2._3 }
      }
  }

}
