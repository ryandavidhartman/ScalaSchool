package kvstore

import akka.actor._
import kvstore.Arbiter._
import scala.collection.immutable.Queue
import akka.actor.SupervisorStrategy.Restart
import scala.annotation.tailrec
import akka.pattern.{ ask, pipe }
import scala.concurrent.duration._
import akka.util.Timeout
import akka.event.LoggingReceive
import collection.mutable.{ HashMap, MultiMap }
import collection.immutable.Set
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


//Primary Send Message List: ( To Client: OperationAck(id), OperationFailed(id), GetResult(key, valueOption, id) )
//contd//
//Primary Receive Message List: (From Client : INSERT(key, value, id), REMOVE(key, id), Get(key, id))
//Secondary Send Message List: ( To Client: GetResult(key, valueOption, id) )
//Secondary Receive Message List: (From Client :  Get(key, id))
class Replica(val arbiter: ActorRef, persistenceProps: Props)
        extends Actor
        with ActorLogging
        with Primary
        with Secondary{
  import Replica._
  import Replicator._
  import Persistence._
  import context.dispatcher

  /*
   * The contents of this actor is just a suggestion, you can implement it in any way you like.
   */
  
  var cache = Map.empty[String, String]



  val persistor = context.system.actorOf(persistenceProps)
  context.watch(persistor)


  //Key is id of the operation: insert, remove, get
  //Value is (ClientRef, NumberOfSecondaryRefs at point of replication request to replicator)
  var replicationAcks = Map.empty[Long, (ActorRef, Long)]
  //Key is replicator actor ref
  var replicatorAcks = new HashMap[ActorRef, collection.mutable.Set[Long]] with MultiMap[ActorRef, Long]

  arbiter ! Join





  def receive = LoggingReceive {
    case JoinedPrimary   => context.become(leader)
    case JoinedSecondary => context.become(secondaryReplica)
  }





}
