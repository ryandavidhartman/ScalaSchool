package nodescala

import scala.language.postfixOps
import scala.concurrent._
import scala.concurrent.duration._
import ExecutionContext.Implicits.global

object Main {

  def main(args: Array[String]) {

    val myServer = new NodeScala.Default(8191)
    val myServerSubscription = myServer.createListener("/test")
    val subscription = myServerSubscription.start

    val userInterrupted: Future[String] = {
      Future.userInput("Enter your message: ").continueWith(f => "You entered... " + f.value.get.get)
    }

    val timeOut: Future[String] = {
      Future.delay(10 second).continue(p => "Server timeout!")
    }

    val terminationRequested: Future[String] = {
      val fs = List(timeOut, userInterrupted)
      Future.any(fs)
    }

    terminationRequested onSuccess {
      case msg => {
        subscription.unsubscribe()
        println(msg)
        println("Bye!")
      }
    }
  }

}