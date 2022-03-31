package kvstore

import akka.actor.Props
import akka.actor.Actor
import akka.actor.ActorRef
import scala.concurrent.duration._
import scala.language.postfixOps

object Replicator {
  case class Replicate(key: String, valueOption: Option[String], id: Long)
  case class Replicated(key: String, id: Long)

  case class Snapshot(key: String, valueOption: Option[String], seq: Long)
  case class SnapshotAck(key: String, seq: Long)

  def props(replica: ActorRef): Props = Props(new Replicator(replica))
}

class Replicator(val replica: ActorRef) extends Actor {
  import Replicator._
  import Replica._
  import context.dispatcher

  /*
   * The contents of this actor is just a suggestion, you can implement it in any way you like.
   */

  // map from sequence number to pair of sender and request
  var acks = Map.empty[Long, (ActorRef, Replicate)]
  // a sequence of not-yet-sent snapshots (you can disregard this if not implementing batching)
  var pending = Map.empty[Long, Snapshot]

  var _seqCounter = 0L
  def nextSeq = {
    val ret = _seqCounter
    _seqCounter += 1
    ret
  }

  /* TODO Behavior for the Replicator. */
  def receive: Receive = {
    case Replicate(key, valueOptions, id) =>
      val seq = nextSeq
      acks += seq -> (replica, Replicate(key, valueOptions, id))
      pending += seq -> Snapshot(key, valueOptions, id)
    case Replicated(key, id) =>
      acks -= id
    case SnapshotAck(key, id) =>
      self ! Replicated(key, id)
    case _ =>
      println("test replecatotr")
  }

  context.system.scheduler.schedule(0 milliseconds, 100 milliseconds) {
    pending foreach (p => {
      if (acks.contains(p._1)) {
        val ack = acks.get(p._1)
        ack.get._1 ! p._2
      } else
        pending -= p._1
    })
  }

}
