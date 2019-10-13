package kvstore

import akka.actor.{Actor, ActorRef, Kill, OneForOneStrategy, PoisonPill, Props, SupervisorStrategy, Terminated}
import kvstore.Arbiter._
import akka.pattern.{ask, pipe}
import akka.stream.Supervision.Stop

import scala.concurrent.duration._
import akka.util.Timeout
import kvstore.Persistence.Persist
import kvstore.Replicator.Replicate

import scala.Tuple2

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

  val MAX_RETRIES = 10
  case class PersistInfo(persist: Persist, caller: ActorRef, retries: Int = 0)
  case class ReplicateInfo(replicate: Replicate, caller: ActorRef, replicator: ActorRef, originalSeq: Long, retries: Int = 0)
}

class Replica(val arbiter: ActorRef, persistenceProps: Props) extends Actor {
  import Replica._
  import Replicator._
  import Persistence._
  import context.dispatcher

  /*
   * The contents of this actor is just a suggestion, you can implement it in any way you like.
   */

  var kv = Map.empty[String, String]
  // a map from secondary replicas to replicators
  var secondaries = Map.empty[ActorRef, ActorRef]
  // the current set of replicators
  var replicators = Set.empty[ActorRef]
  var replicationId = -1L
  var replicationAcks = Map.empty[Long, ReplicateInfo]

  var expectedSeq:Long = 0

  val persistence: ActorRef = context.actorOf(persistenceProps)
  var persistenceAcks = Map.empty[Long, PersistInfo]


  override def preStart(): Unit = {
    arbiter ! Join
    context.system.scheduler.schedule(0 milliseconds, 100 milliseconds)(resendUnAcknowledgedMsgs())
  }


  def receive = {
    case JoinedPrimary   => context.become(leader)
    case JoinedSecondary => context.become(replica)
  }

  /* TODO Behavior for  the leader role. */
  val leader: Receive = {
    case Insert(key: String, value: String, id: Long) =>
      kv += (key -> value)
      val persistMsg = Persist(key, Option(value), id)
      persistence !  persistMsg
      persistenceAcks += id -> PersistInfo(persistMsg, sender)
      replicators.foreach{ r =>
        replicationId += 1
        val replicateMsg = Replicate(key, Some(value), replicationId)
        replicationAcks += replicationId -> ReplicateInfo(replicateMsg, sender, r, id)
        println(s"waiting on n: ${replicationAcks.toList.length} replication acks")
        r ! replicateMsg

      }
    case Remove(key: String, id: Long) =>
      kv -= key
      val persistMsg = Persist(key, None, id)
      persistence !  persistMsg
      persistenceAcks += id -> PersistInfo(persistMsg, sender)
      replicators.foreach{ r =>
        replicationId += 1
        val replicateMsg = Replicate(key, None, replicationId)
        replicationAcks += replicationId -> ReplicateInfo(replicateMsg, sender, r, id)
        r ! replicateMsg
      }
    case Get(key: String, id: Long) =>
      sender ! GetResult(key, kv.get(key), id)
    case Replicas(latestReplicas) =>
      val latestSecondaries = latestReplicas - self
      val currentSecondaries = secondaries.keySet
      val addedSecondaries = latestSecondaries -- currentSecondaries
      val removedSecondaries = currentSecondaries -- latestSecondaries

      // Handle new replicas
      addedSecondaries.foreach{ addedSecondary =>
        val replicator = context.actorOf(Replicator.props(addedSecondary))
        replicators += replicator
        secondaries += (addedSecondary -> replicator)
        kv.foreach{ kv =>
          replicationId += 1
          val replicateMsg = Replicate(kv._1, Some(kv._2), replicationId)
          replicator ! replicateMsg
          replicationAcks += replicationId -> ReplicateInfo(replicateMsg, self, replicator, replicationId)
        }

      }
      // Handle removed replicas
    removedSecondaries.foreach{ removedSecondary =>
      val replicator = secondaries(removedSecondary)
      replicator ! Kill
      secondaries -= removedSecondary
      replicators -= replicator
    }
    case Persisted(key, id) =>
      val persistenceInfo  = persistenceAcks(id)
      persistenceInfo.persist.valueOption.foreach(v => kv += (key -> v))
      if(!replicationAcks.values.exists(_.originalSeq == id)) persistenceInfo.caller ! OperationAck(id)
      persistenceAcks -= id
    case Replicated(key, id) =>
      val replicationInfo = replicationAcks(id)
      replicationAcks -= id
      val persistenceDone = persistenceAcks.get(replicationInfo.originalSeq).isEmpty
      val replicationDone = !replicationAcks.values.exists(_.originalSeq == replicationInfo.originalSeq)
      if(persistenceDone&& replicationDone) replicationInfo.caller ! OperationAck(replicationInfo.originalSeq)

  }

  /* TODO Behavior for the replica role. */
  val replica: Receive = {
    case Get(key: String, id: Long) =>
      sender ! GetResult(key, kv.get(key), id)
    case Snapshot(key, valueOption, seq) =>
      println("The secondary got a snapshot request")
      handleSnapshotSeq(seq, key, valueOption, sender)
    case Persisted(key, seq) =>
      persistenceAcks.get(seq).foreach(_.caller ! SnapshotAck(key, seq))
      persistenceAcks -= seq
  }

  private def handleSnapshotSeq(seq: Long, key:String, valueOption: Option[String], sender:ActorRef): Unit =
    if(seq == expectedSeq) {
      val persistMsg = Persist(key, valueOption, seq)
      persistence !  persistMsg
      persistenceAcks += seq -> PersistInfo(persistMsg, sender)
      expectedSeq += 1
      valueOption match {
        case Some(v) => kv += (key -> v)
        case None =>  kv -= key
      }
    } else if (seq < expectedSeq) {
      sender ! SnapshotAck(key, seq)
    }


  private def resendUnAcknowledgedMsgs():Unit = {
    println(s"resendUnAcknowledgedMsgs-> persistenceAcks: ${persistenceAcks.toList.length} replicationAcks: ${replicationAcks.toList.length}")
    persistenceAcks.foreach {
      case (id, pi) =>
        println("sending a retry for persistence")
        val retries = pi.retries+1
        if(retries < MAX_RETRIES) {
          persistence ! pi.persist
          persistenceAcks += (id -> pi.copy(retries = retries))
        } else {
          persistenceAcks -= id
          pi.caller ! OperationFailed(id)
        }
    }
    replicationAcks.foreach {
      case (id, ri) =>
        println(s"sending a retry for replication retry: ${ri.retries}")
        if( ri.retries < MAX_RETRIES) {
          ri.replicator ! ri.replicate
          replicationAcks += (id -> ri.copy(retries = ri.retries+1))
        } else {
          val replicateInfo = replicationAcks(id)
          replicationAcks -= id
          ri.caller ! OperationFailed(replicateInfo.originalSeq)
        }
    }
  }

}

