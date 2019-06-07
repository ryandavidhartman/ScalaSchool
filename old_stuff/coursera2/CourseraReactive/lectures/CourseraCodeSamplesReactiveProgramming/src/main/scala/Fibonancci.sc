import akka.actor.Status.Success
import scala.concurrent.{Promise, Future}
import scala.util.Failure

object Fibonancci {
  def classic(n: Int): Int = n match {
    case i if i  < 0 => throw new IllegalArgumentException(s"n must zero or greater")
    case 0 => 0
    case 1 => 1
    case x => classic(x-1) + classic(x-2)
  }
  val bob = classic(7)

  def junk(n: Int): Future[]


}