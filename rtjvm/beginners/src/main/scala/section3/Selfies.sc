import java.util.concurrent.ForkJoinPool

import scala.concurrent.{ExecutionContext, Future}

implicit val ec = ExecutionContext.fromExecutor(new ForkJoinPool(4))

abstract class Food(val name: String) {
  override def toString = name
}
