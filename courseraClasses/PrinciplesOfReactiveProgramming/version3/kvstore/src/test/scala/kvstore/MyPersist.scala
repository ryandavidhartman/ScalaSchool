package kvstore


import akka.actor.{Props, Actor}
import scala.util.Random
import java.util.concurrent.atomic.AtomicInteger
import akka.actor.ActorLogging

object MyPersist {
  case class Persist(key: String, valueOption: Option[String], id: Long)
  case class Persisted(key: String, id: Long)

  class PersistenceException extends Exception("Persistence failure")

  def props(flaky: Boolean): Props = Props(classOf[Persistence], flaky)
}

//Send Message List:
//Receive Message List:
class MyPersist(flaky: Boolean) extends Actor with ActorLogging {
  import Persistence._
  val random = new Random((/*System.nanoTime() % 100*/1).toInt)
  def receive = {
    case Persist(key, _, id) =>
      if (random.nextBoolean()) sender ! Persisted(key, id)
      else throw new PersistenceException
  }

}
