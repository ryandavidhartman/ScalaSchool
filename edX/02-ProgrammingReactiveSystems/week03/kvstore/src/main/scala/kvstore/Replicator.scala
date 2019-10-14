package kvstore

import akka.actor.{Actor, ActorRef, Props}

object Replicator {
  case class Replicate(key: String, valueOption: Option[String], id: Long)
  case class Replicated(key: String, id: Long)

  case class Snapshot(key: String, valueOption: Option[String], seq: Long)
  case class SnapshotAck(key: String, seq: Long)

  def props(replica: ActorRef): Props = Props(new Replicator(replica))
}

class Replicator(val replica: ActorRef) extends Actor {
  import Replicator._
  import scala.concurrent.duration._
  import scala.concurrent.ExecutionContext.Implicits.global

  /*
   * The contents of this actor is just a suggestion, you can implement it in any way you like.
   */

  // map from sequence number to pair of sender and request
  var acks = Map.empty[Long, (ActorRef, Replicate)]
  // a sequence of not-yet-sent snapshots (you can disregard this if not implementing batching)
  var pending = Vector.empty[Snapshot]

  var _seqCounter = 0L
  def nextSeq = {
    val ret = _seqCounter
    _seqCounter += 1
    ret
  }

  context.system.scheduler.schedule(0.millisecond, 100.milliseconds) {
    acks foreach {
      case (seq, (_, Replicate(key, valueOption, _))) =>
        replica ! Snapshot(key, valueOption, seq)
    }
  }

  def receive: Receive = {
    case rep @ Replicate(key, valueOption, id) =>
      val seq = nextSeq
      replica ! Snapshot(key, valueOption, seq)
      acks += seq -> Tuple2(sender(), rep)

    case SnapshotAck(key, seq) =>
      if (acks.contains(seq)) {
        val (sender, Replicate(key, _, id)) = acks(seq)
        acks -= seq
        sender ! Replicated(key, id)
      }
  }
}
