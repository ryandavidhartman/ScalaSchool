package reactive

import java.util.concurrent.Executor

import akka.actor.{Actor, ActorRef}
import reactive.BadCache2.Result
import reactive.Receptionist.Get
import akka.pattern.pipe

import scala.concurrent.ExecutionContext

/*
When futures and actors are mixed you can also have synchronization issues.
Since future composition methods tempt you to close over the actor's state
 */

class BadCache extends Actor {
  var cache = Map.empty[String, String]

  implicit val executor: Executor with ExecutionContext = context.dispatcher.asInstanceOf[Executor with ExecutionContext]


  override def receive: Receive = {
    case Get(url) =>
      if(cache contains url )
        sender ! cache(url)
      else
        WebClient get url foreach { body =>
          // This happens in the scope of the callback on the future
          // not thread safe with the actor!
          cache += url -> body
          sender ! body
        }
  }

}

object BadCache2 {
  case class Result(sender: ActorRef, url: String, body: String)
}

class BadCache2 extends Actor {
  var cache = Map.empty[String, String]

  implicit val executor: Executor with ExecutionContext = context.dispatcher.asInstanceOf[Executor with ExecutionContext]


  override def receive: Receive = {
    case Get(url) =>
      if(cache contains url )
        sender ! cache(url)
      else
        WebClient get url map { r => Result(sender, url, r) } pipeTo self
    case Result(client, url, body) =>
      cache += url -> body
      sender ! body
  }

  /*But this actor contains another problem.
The transformation described by the map operation on the future,
runs the code which you give it, in the future.
And that means that, the sender will be accessed.
In the future.
This is problematic, because sender is giving
you the actor which corresponds to the actor, which has sent the message, which is currently
being processed, but when that future runs
the actor might do something completely different*/

}
