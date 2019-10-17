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

  case class PersistInfo(originalSender: ActorRef, originalId:Long, persistMsg: Persist)
  case class ReplicateInfo(originalSender: ActorRef, originalId: Long, stillReplicating: Set[ActorRef])
}

class Replica(val arbiter: ActorRef, persistenceProps: Props) extends Actor {

  /*
   * The contents of this actor is just a suggestion, you can implement it in any way you like.
   */

  var kv = Map.empty[String, String]
  // a map from secondary replicas to replicators
  var secondaries = Map.empty[ActorRef, ActorRef]
  // the current set of replicators
  var replicators = Set.empty[ActorRef]
  // map from persist sequence number to the original sender, original id, and persist message to send
  var persistAcks = Map.empty[Long, PersistInfo]
  // map from sequence number to tuple of sender, id, and set of not replicated replicators
  var replicationAcks = Map.empty[Long, ReplicateInfo]

  var persistActor = context.actorOf(persistenceProps)
  context.watch(persistActor)

  override def preStart(): Unit = {
    arbiter ! Join
    context.system.scheduler.schedule(initialDelay = 0 milliseconds, interval = 100 milliseconds) {
      persistAcks.foreach { case (_, PersistInfo(_, _, msg)) => persistActor ! msg }
    }
  }

  var _seqCounter = 0L
  def nextSeq:Long = {
    val ret = _seqCounter
    _seqCounter += 1
    ret
  }


  def receive = {
    case JoinedPrimary   => context.become(leader)
    case JoinedSecondary => context.become(replica(0))
  }

  def revertFailedOperation(seq: Long, key: String, oldValue: Option[String]): Unit = {
    val persistFailed = if (persistAcks.contains(seq)) {
      val PersistInfo(originalSender, originalId, _) = persistAcks(seq)
      if (originalSender != self) originalSender ! OperationFailed(originalId)
      persistAcks -= seq
      true
    } else false

    val replicateFailed = if (replicationAcks.contains(seq)) {
      val ReplicateInfo(originalSender, originalId, _) = replicationAcks(seq)
      if (originalSender != self) originalSender ! OperationFailed(originalId)
      replicationAcks -= seq
      true
    } else false

    if (persistFailed || replicateFailed)
      oldValue.fold(self ! Remove(key, nextSeq)){value => self ! Insert(key, value, nextSeq)}
  }

  val leader: Receive = {
    case Insert(key, value, id) =>
      val oldValue = kv.get(key)
      kv += key -> value
      val seq = if (id >= 0) nextSeq else id
      val persist = Persist(key, Some(value), seq)
      persistAcks += seq -> PersistInfo(sender(), id, persist)
      persistActor ! persist
      if (replicators.nonEmpty) {
        replicationAcks += seq -> ReplicateInfo(sender(), id, replicators)
      }
      replicators foreach { _ ! Replicate(key, Some(value), seq) }
      context.system.scheduler.scheduleOnce(1.second) {
        revertFailedOperation(seq, key, oldValue)
      }

    case Remove(key, id) =>
      val oldValue = kv.get(key)
      kv -= key
      val seq = if(id >= 0) nextSeq else id
      val persist = Persist(key, None, seq)
      persistAcks += seq -> PersistInfo(sender(), id, persist)
      persistActor ! persist
      if (replicators.nonEmpty) {
        replicationAcks += seq -> ReplicateInfo(sender(), id, replicators)
      }
      replicators foreach { _ ! Replicate(key, None, seq) }
      context.system.scheduler.scheduleOnce(1.second) {
        revertFailedOperation(seq, key, oldValue)
      }

    case Get(key, id) =>
      sender() ! GetResult(key, kv.get(key), id)

    case Replicas(replicas) =>
      replicationAcks foreach {
        case (seq, ReplicateInfo(sender, id, _)) =>
          if (!persistAcks.contains(seq)) {
            sender ! OperationAck(id)
          }
      }
      replicationAcks = Map.empty[Long, ReplicateInfo]
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
        val ReplicateInfo(originalSender, id, reps) = replicationAcks(seq)
        replicationAcks += seq -> ReplicateInfo(originalSender, id, reps - context.sender())
        if (replicationAcks(seq).stillReplicating.isEmpty) {
          replicationAcks -= seq
          if (!persistAcks.contains(seq)) {
            originalSender ! OperationAck(id)
          }
        }
      }

    case Persisted(_, seq) =>
      val PersistInfo(sender, id, _) = persistAcks(seq)
      persistAcks -= seq
      if (!replicationAcks.contains(seq)) {
        sender ! OperationAck(id)
      }

    case Terminated(actorWhoDied) =>
      if (actorWhoDied == persistActor) {
        persistActor = context.actorOf(persistenceProps)
        context.watch(persistActor)
        persistAcks foreach { persistActor ! _._2.persistMsg }
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
          persistAcks += seq -> PersistInfo(sender(), id, persist)
        } else {
          sender() ! SnapshotAck(key, id)
        }
        context.become(replica(math.max(expectedId, id + 1)))
      }

    case Persisted(key, seq) =>
      val PersistInfo(originalSender, id, _) = persistAcks(seq)
      persistAcks -= seq
      originalSender ! SnapshotAck(key, id)

    case Terminated(actorWhoDied) =>
      if (actorWhoDied == persistActor) {
        persistActor = context.actorOf(persistenceProps)
        context.watch(persistActor)
        persistAcks.foreach{ case (_, PersistInfo(_, _, persistMsg)) => persistActor ! persistMsg }
      }
  }

}
