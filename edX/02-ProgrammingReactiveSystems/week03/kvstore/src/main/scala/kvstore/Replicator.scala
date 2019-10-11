package kvstore

import akka.actor.Props
import akka.actor.Actor
import akka.actor.ActorRef
import scala.concurrent.duration._

object Replicator {
  case class Replicate(key: String, valueOption: Option[String], id: Long)
  case class Replicated(key: String, id: Long)

  case class Snapshot(key: String, valueOption: Option[String], seq: Long)
  case class SnapshotAck(key: String, seq: Long)

  def props(replica: ActorRef): Props = Props(new Replicator(replica))
}

class Replicator(val replica: ActorRef) extends Actor {
  import Replicator._
  import context.dispatcher

  /*
   * The contents of this actor is just a suggestion, you can implement it in any way you like.
   */

  override def preStart():Unit = {
    context.system.scheduler.schedule(0 milliseconds, 100 milliseconds)(resendSnapShotMsgs)
  }

  // map from sequence number to pair of sender and request
  var acks = Map.empty[Long, (ActorRef, Replicate)]
  // a sequence of not-yet-sent snapshots (you can disregard this if not implementing batching)
  var pending = Vector.empty[Snapshot]

  var _seqCounter = 0L
  def nextSeq() = {
    val ret = _seqCounter
    _seqCounter += 1
    ret
  }


  /* TODO Behavior for the Replicator. */
  def receive: Receive = {
    case replicateMsg @ Replicate(key, valueOption, _) =>
      val seq = nextSeq()
      val senderMsg = (sender, replicateMsg)
      acks += seq -> senderMsg
      sendSnapShotMsg(key, valueOption, seq)
    case SnapshotAck(key, seq) =>
      println("Got a snapshot ACK")
      acks.get(seq).foreach ( _._1 ! Replicated(key, seq))
      acks -= seq
    case _ =>
  }

  private def resendSnapShotMsgs():Unit = acks.foreach {
    case (seq, (_, msg)) => sendSnapShotMsg(msg.key, msg.valueOption, seq)
  }


  private def sendSnapShotMsg(key:String, valueOpt: Option[String], seq: Long): Unit = {
    println(s"Sending a snapshot from the replicator key:$key value:$valueOpt seq:$seq replica: $replica")
    replica ! Snapshot(key, valueOpt, seq)
  }

}
