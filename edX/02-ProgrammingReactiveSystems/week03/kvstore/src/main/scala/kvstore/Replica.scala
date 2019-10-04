package kvstore

import akka.actor.{Actor, ActorRef, OneForOneStrategy, PoisonPill, Props, SupervisorStrategy, Terminated}
import kvstore.Arbiter._
import akka.pattern.{ask, pipe}
import akka.stream.Supervision.Stop

import scala.concurrent.duration._
import akka.util.Timeout

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

  var expectedSeq:Long = -1

  override def preStart(): Unit = arbiter ! Join


  def receive = {
    case JoinedPrimary   => context.become(leader)
    case JoinedSecondary => context.become(replica)
  }

  /* TODO Behavior for  the leader role. */
  val leader: Receive = {
    case Insert(key: String, value: String, id: Long) =>
      insertKey(key, value)
      sender ! OperationAck(id)
    case Remove(key: String, id: Long) =>
      removeKey(key)
      sender ! OperationAck(id)
    case Get(key: String, id: Long) =>
      sender ! GetResult(key, kv.get(key), id)
    case Replicas(latestReplicas) =>
      val currentSecondaries = secondaries.keySet
      val addedSecondaries = latestReplicas -- currentSecondaries
      val removedSecondaries = currentSecondaries -- latestReplicas

      // Handle new replicas
      addedSecondaries.foreach{ addedSecondary =>
        val replicator = context.actorOf(Replicator.props(addedSecondary))
        replicators += replicator
        secondaries += (addedSecondary -> replicator)
      }

     // Handle removed replicas
    removedSecondaries.foreach{ removedSecondary =>
      val replicator = secondaries(removedSecondary)
      replicator ! Stop
      secondaries -= removedSecondary
      replicators -= replicator
    }
  }

  /* TODO Behavior for the replica role. */
  val replica: Receive = {
    case Get(key: String, id: Long) =>
      sender ! GetResult(key, kv.get(key), id)
    case Snapshot(key, valueOption, seq) =>
      handleSnapshotSeq(seq, key, valueOption)
      ackSnapshotIfNecessary(seq, key, sender)
  }

  private def handleSnapshotSeq(seq: Long, key:String, valueOption: Option[String]): Unit =
    if(seq == (expectedSeq+1)) {
      expectedSeq += 1
      valueOption match {
        case Some(v) => insertKey(key, v)
        case None => removeKey(key)
      }
    }

  private def ackSnapshotIfNecessary(seq: Long, key: String, sender: ActorRef): Unit = {
    if(seq <= expectedSeq)
      sender ! SnapshotAck(key, seq)
  }

  private def insertKey(key:String, value:String): Unit = {
    kv += (key -> value)
  }
  private def removeKey(key: String): Unit = {
    kv -= key
  }

}

