import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
import org.scalatest.time.SpanSugar.convertIntToGrainOfTime

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.language.postfixOps

trait ConcurrentUtils {
  def timed[T](block: => T): T = {
    val start = System.currentTimeMillis()
    val result = block
    val duration = System.currentTimeMillis() - start
    println(s"Time taken: $duration")
    result
  }
}

class FutureTests extends AnyFlatSpec  with ConcurrentUtils{

  implicit val ec = ExecutionContext.fromExecutor(new java.util.concurrent.ForkJoinPool(8))

  def f10(): Future[Int] = Future {Thread.sleep(2000); 10}
  def f20(): Future[Int] = Future {Thread.sleep(4000); 20}

  "Futures" should "run in parallel if declared outside the for" in {
    val future10: Future[Int] = f10()

    val future20: Future[Int] = f20()

    val resultF = for {
      r1 <- future10
      r2 <- future20
    } yield r1+r2

    val result = timed(Await.result(resultF, 4 seconds))
    result shouldBe 30
  }

  "Futures" should "run in series if declared inside the for" in {
    val resultF = for {
      r1 <- f10()
      r2 <- f20()
    } yield r1+r2

    val result = timed(Await.result(resultF, 6 seconds))
    result shouldBe 30
  }
}
