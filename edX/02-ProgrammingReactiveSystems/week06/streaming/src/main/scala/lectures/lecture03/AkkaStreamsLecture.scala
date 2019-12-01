package lectures.lecture03

import akka.NotUsed
import akka.actor._
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Flow, Sink, Source}

import scala.concurrent.Future

/**
Canonical Akka Streams Example
 **/
object AkkaStreamsLecture {

  implicit val system: ActorSystem = ActorSystem()
  implicit val mat: ActorMaterializer= ActorMaterializer()

  val eventuallyResult: Future[Int] =
    Source(1 to 10)
      .map(_ * 2)
      .runFold(zero = 0)(_ + _)

  val eventuallyResultLessSugar: Future[Int] =
    Source(1 to 10)
      .map(_ * 2)
      .runWith {Sink.fold(zero = 0) { (acc: Int, x: Int) => acc + x }}


  /* now lets do the same but extract the "doubling" function out into a reusable
     flow
   */

  val numbersSource: Source[Int, _] = Source(iterable = 1 to 10)
  val doublingFlow: Flow[Int, Int, _] = Flow.fromFunction(((x:Int) => x * 2))
  val sumSink: Sink[Int, Future[Int]] = Sink.fold(zero = 0)((acc:Int, x:Int) => acc + x)

  val eventuallyResultWithFlow: Future[Int] = numbersSource.via(doublingFlow).runWith(sumSink)
}
