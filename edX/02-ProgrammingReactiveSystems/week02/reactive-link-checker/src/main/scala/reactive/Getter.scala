package reactive

import java.util.concurrent.Executor

import akka.actor.{Actor, PoisonPill, Status}
import akka.pattern.pipe
import org.jsoup.Jsoup

import scala.collection.JavaConverters._
import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

object Getter {
  case object Done
  case object Abort
}

class Getter(url: String, depth: Int) extends Actor {

  import Getter._

  implicit val executor: Executor with ExecutionContext = context.dispatcher.asInstanceOf[Executor with ExecutionContext]

  WebClient get url pipeTo self

  /*
  WebClient get url pipeTo self is equivalent to this:


  val future = WebClient.get(url)
  future.onComplete {
    case Success(body) => self ! body
    case Failure(err) => self ! Status.Failure(err)
  }

  which is also equivalent to
  val future = WebClient.get(url)
  future.pipeTo(self)

   */







  def receive: PartialFunction[Any, Unit] = {
    case body: String =>
      for (link <- findLinks(body))
        context.parent ! Controller.Check(link, depth)
      stop()
    case _: Status.Failure => stop()
    case Abort => stop()
  }

  def stop(): Unit = {
    context.parent ! Done
    context.stop(self)  //  self ! PoisonPill  see https://stackoverflow.com/questions/13847963/akka-kill-vs-stop-vs-poison-pill
  }

/*
Finding links with regex
val A_TAG = "(?i)<a ([^>]+)>.+?</a>".r
  val HREF_ATTR = """\s*(?i)href\s*=\s*(?:"([^"]*)"|'([^']*)'|([^'">\s]+))\s*""".r

  def findLinks(body: String): Iterator[String] = {
    for {
      anchor <- A_TAG.findAllMatchIn(body)
      HREF_ATTR(dquot, quot, bare) <- anchor.subgroups
    } yield if (dquot != null) dquot
    else if (quot != null) quot
    else bare
  }*/

  def findLinks(body: String): Iterator[String] = {
    val document = Jsoup.parse(body, url)
    val links = document.select("a[href]")
    for {
      link <- links.iterator().asScala
    } yield link.absUrl("href")
  }

}
