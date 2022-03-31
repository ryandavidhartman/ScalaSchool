package kvstore

import akka.actor.{ActorRef, Actor}
import scala.collection.immutable

object Arbiter {
  case object Join

  case object JoinedPrimary
  case object JoinedSecondary

  /**
   * This message contains all replicas currently known to the arbiter, including the primary.
   */
  case class Replicas(replicas: Set[ActorRef])
}

/**
 * The Arbiter is created before anything else and it waits for the Replicas to announce themselves.
 * The actor ref of the arbiter is passed to the replicas as they are created.
 * Once the arbiter and at least one replica is created then it is time to start sending insert, remove and get commands
 * First replica becomes the primary replica.
 * Primary replica does not need a replicator.
 * It maintains a cache (key value map in memory). It also persists the key and value to to Persistor.
 * It also maintains the complete set of actor refs to all replicas
 * By sending the Replica message to the primary replica the primary replica is able to derive the secondary refs
 *
 */
//Send Message List: (To Primary Replica: Replicas(Set[ActorRef])) ( To Primary Replica: JoinedPrimary) ( To Secondary Replica: JoinedSecondary)
//Receive Message List: Join
class Arbiter extends Actor {
  import Arbiter._
  var leader: Option[ActorRef] = None
  var replicas = Set.empty[ActorRef]

  def receive = {
    case Join =>
      if (leader.isEmpty) {
        leader = Some(sender)
        replicas += sender
        sender ! JoinedPrimary
      } else {
        replicas += sender
        sender ! JoinedSecondary
      }
      leader foreach { ref =>
        ref ! Replicas(replicas)
      }
  }

}
