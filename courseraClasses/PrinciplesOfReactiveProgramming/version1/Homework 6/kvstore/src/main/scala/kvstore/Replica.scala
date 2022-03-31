package kvstore

import akka.actor.{ OneForOneStrategy, Props, ActorRef, Actor, PoisonPill }
import kvstore.Arbiter._
import scala.collection.immutable.Queue
import akka.actor.SupervisorStrategy.Restart
import scala.annotation.tailrec
import akka.pattern.{ ask, pipe }
import akka.actor.Terminated
import scala.concurrent.duration._
import akka.actor.PoisonPill
import akka.actor.OneForOneStrategy
import akka.actor.SupervisorStrategy
import akka.util.Timeout
import scala.language.postfixOps

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

class Replica(val arbiter: ActorRef, val persistenceProps: Props) extends Actor {
  import Replica._
  import Replicator._
  import Persistence._
  import context.dispatcher

  /*
   * The contents of this actor is just a suggestion, you can implement it in any way you like.
   */
  arbiter ! Join

  var kv = Map.empty[String, String]
  // a map from secondary replicas to replicators
  var secondaries = Map.empty[ActorRef, ActorRef]
  // the current set of replicators
  var replicators = Set.empty[ActorRef]

  var senders = Map.empty[Long, ActorRef]
  var pending = Map.empty[Long, (ActorRef, Persist)]

  var counters = Map.empty[Long, Integer]

  private var seq = -1L

  def receive = {
    case JoinedPrimary => context.become(leader)
    case JoinedSecondary => context.become(replica)
  }

  /* TODO Behavior for  the leader role. */
  val leader: Receive = {
    case Get(key, id) =>
      sender ! GetResult(key, kv.get(key), id)
    case Insert(key, value, id) =>
      kv += key -> value
      primaryPersist(key, Option(value), id)
    case Remove(key, id) =>
      kv -= key
      primaryPersist(key, None, id)
    case Persisted(key, id) =>
      senders.get(id).get ! OperationAck(id)
      senders -= id
      println("test leader persisted:" + key + " " + id)
    case Replicas(replicas) =>
      replicators foreach (r => r.tell(PoisonPill, r))
      replicators = replicas
      if (!kv.isEmpty)
        applyReplicas(kv.last._1, Option(kv.last._2), 0L)

      println("test leader replicas")
    case SnapshotAck(key, id) =>
      println("snapshot counters:" + counters+", id:" + id )
      
      if (counters.contains(id)) {
        val c = counters.get(id).get
        if (c - 1 == 0) {
          println("snapshot operation ack senders", senders.get(id).get )
          senders.get(id).get ! OperationAck(id)
          senders -= id
          counters -= id
        } else {
          counters -= id
          counters += id -> (c - 1)
        }
      }
      println("SnapshotAck(key, id)")
    case _ =>
      println("test leader")
  }

  def primaryPersist(key: String, valueOption: Option[String], id: Long): Unit = {

    if (!replicators.isEmpty) {
      applyReplicas(key, valueOption, id)
      counters += id -> replicators.tail.size
      waitSecondaries(id, sender)
    } else {
      val persistence = context.system.actorOf(persistenceProps)
      pending += id -> (persistence, Persist(key, valueOption, id))
      senders += id -> sender

      waitPrimary(id)
    }
  }

  def applyReplicas(key: String, valueOption: Option[String], id: Long): Unit = {
    replicators.tail foreach (r => {
      r ! Snapshot(key, valueOption, id)
      senders += id -> sender
    })
  }

  def waitPrimary(id: Long): Unit = {
    context.system.scheduler.scheduleOnce(1 second)({
      if (senders.contains(id)) {
        senders.get(id).get ! OperationFailed(id)
        senders -= id
        pending -= id
      }
    })
  }

  def waitSecondaries(id: Long, sender: ActorRef): Unit = {
    context.system.scheduler.scheduleOnce(1 second)({
      println ("secondaries, 1 second left", counters)
      if (counters.contains(id) && counters.get(id).get > 0) {
        sender ! OperationFailed(id)
      }
      counters -= id
    })
  }

  /* TODO Behavior for the replica role. */
  val replica: Receive = {
    case Get(key, id) =>
      sender ! GetResult(key, kv.get(key), id)
      println("test replica get:" + key)
    case Snapshot(key, valueOption, id) =>
      println("snapshot key:" + key + ", id:" + id)

      if (seq + 1 >= id) {
        if (id > seq) {
          if (valueOption.nonEmpty) {
            val value = valueOption.get
            kv += key -> value
          } else {
            if (kv.contains(key))
              kv -= key
          }
          seq = id
        }

        println("replica persist")
        val persistence = context.system.actorOf(persistenceProps)
        pending += id -> (persistence, Persist(key, valueOption, id))
        senders += id -> sender
      }
    case Persisted(key, id) =>
      senders.get(id).get ! SnapshotAck(key, id)
      senders -= id
      println("test replica persisted:" + key + " " + id)
    case _ =>
      println("test replica")
  }

  context.system.scheduler.schedule(0 milliseconds, 50 milliseconds) {
    pending foreach (p => {
      println("pending senders", senders)
      if (senders.contains(p._1)) {
        p._2._1 ! p._2._2
      } else
        pending -= p._1
    })
  }

}
