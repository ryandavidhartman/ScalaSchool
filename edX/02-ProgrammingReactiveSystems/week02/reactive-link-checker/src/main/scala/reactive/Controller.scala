package reactive

import akka.actor.Actor
import akka.actor.Props
import akka.actor.Terminated
import akka.actor.SupervisorStrategy
import akka.actor.ActorLogging
import akka.actor.ReceiveTimeout
import scala.concurrent.duration._
import akka.actor.ActorRef

object Controller {
  case class Check(url: String, depth: Int)
  case class Result(links: Set[String])
}

class Controller extends Actor with ActorLogging {
  import Controller._

  // In the lecture Roland makes a point that it is much
  // better to use a variable pointing to an immutable set
  // than a val pointing to a mutable set for the cache
  //

  /*
  For example, this controller might be used
to process another query, keeping the cache.
Would be a valid use case, but then the result which had been sent back
to the parent, points to the same set, which is mutated by this actor.
And then the other actor will be confused, as to
what the contents of the set are. It is much better to prefer using
variables here, which point to immutable data structures.
This way they can safely be shared as is done here.
   */

  var cache = Set.empty[String]
  var children = Set.empty[ActorRef]

  // Start a 10 second timer after *any* message is processed
  context.setReceiveTimeout(10.seconds)



  /* Alternatively

  import context.dispatcher
  context.system.scheduler.scheduleOnce(10.seconds, self, ReceiveTimeout)

  //Do not do this:
  import context.dispatcher
  context.system.scheduler.scheduleOnce(10.seconds) {
    context.children foreach (_ ! Getter.Abort)
  }

  The above it NOT thread safe!  It is executed context of the scheduler not
  this actor.  Therefore the children is used on another thread
*/


  def receive: PartialFunction[Any, Unit] = {
    case Check(url, depth) =>
      log.debug("{} checking {}", depth, url)
      if (!cache(url) && depth > 0)
        children += context.actorOf(Props(new Getter(url, depth - 1)))
      cache += url
    case Getter.Done =>
      children -= sender
      if (children.isEmpty)
        context.parent ! Result(cache)
    case ReceiveTimeout =>
      context.children foreach (_ ! Getter.Abort)
  }

}
