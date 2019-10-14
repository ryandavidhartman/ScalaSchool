package kvstore

import akka.actor.{Actor, ActorRef, Props}
import kvstore.Replicator._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

object Replicator {
  case class Replicate(key: String, valueOption: Option[String], id: Long)
  case class Replicated(key: String, id: Long)

  case class Snapshot(key: String, valueOption: Option[String], seq: Long)
  case class SnapshotAck(key: String, seq: Long)
  case class SnapshotInfo(originalSender: ActorRef, originalMessage: Replicate)

  def props(replica: ActorRef): Props = Props(new Replicator(replica))
}

class Replicator(val replica: ActorRef) extends Actor {

  // map from the snapshot sequence number to pair of the original sender and original request
  var snapshotAcks = Map.empty[Long, SnapshotInfo]

  var _seqCounter = 0L
  def nextSnapshotId: Long = {
    val ret = _seqCounter
    _seqCounter += 1
    ret
  }

  context.system.scheduler.schedule(0.millisecond, 100.milliseconds) {
    snapshotAcks foreach {
      case (snapshotId, SnapshotInfo(_, Replicate(key, valueOption, _))) =>
        replica ! Snapshot(key, valueOption, snapshotId)
    }
  }

  def receive: Receive = {
    case originalReplicateMsg @ Replicate(key, valueOption, _) =>
      val snapshotId = nextSnapshotId
      replica ! Snapshot(key, valueOption, snapshotId)
      snapshotAcks += snapshotId -> SnapshotInfo(sender, originalReplicateMsg)

    case SnapshotAck(key, seq) =>
      snapshotAcks.get(seq).foreach { ri =>
        snapshotAcks -= seq
        ri.originalSender ! Replicated(key, ri.originalMessage.id)
      }
  }
}
