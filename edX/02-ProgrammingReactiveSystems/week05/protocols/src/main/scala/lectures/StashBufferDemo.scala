package lectures

import akka.actor.typed.{ActorSystem, Behavior}
import akka.actor.typed.scaladsl._

object StashBufferDemo {
    val running: Behaviors.Receive[String] = Behaviors.receiveMessage[String] { msg =>
        println(s"Hello $msg!")
        if (msg == "stop") Behaviors.stopped else Behaviors.same
    }

    val initial: Behavior[String] = Behaviors.setup[String] { ctx =>
        val buffer = StashBuffer[String](100)

        Behaviors.receiveMessage {
            case "first" =>
                buffer.unstashAll(ctx, running)
            case other =>
                buffer.stash(other)
                Behaviors.same
        }
    }

    def main(args: Array[String]): Unit = {
        val system = ActorSystem(initial, "StashBuffer")
        system ! "World"
        system ! "stop"
        system ! "fancy" // will not be processed
        system ! "first"
    }
}
