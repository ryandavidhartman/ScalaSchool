import scala.language.postfixOps
import scala.util._
import scala.util.control.NonFatal
import scala.concurrent._
import scala.concurrent.duration._
import ExecutionContext.Implicits.global
import scala.async.Async.{ async, await }
import scala.collection.mutable.ListBuffer

/**
 * Contains basic data types, data structures and `Future` extensions.
 */
package object nodescala {

  /**
   * Adds extensions methods to the `Future` companion object.
   */
  implicit class FutureCompanionOps[T](val f: Future.type) extends AnyVal {

    /**
     * Returns a future that is always completed with `value`.
     */
    def always[T](value: T): Future[T] = {
      Promise[T]().success(value).future
    }

    /**
     * Returns a future that is never completed.
     *
     *  This future may be useful when testing if timeout logic works correctly.
     */
    def never[T]: Future[T] = {
      Promise[T].failure(new TimeoutException).future
    }

    /**
     * Given a list of futures `fs`, returns the future holding the list of values of all the futures from `fs`.
     *  The returned future is completed only once all of the futures in `fs` have been completed.
     *  The values in the list are in the same order as corresponding futures `fs`.
     *  If any of the futures `fs` fails, the resulting future also fails.
     */
    def all[T](fs: List[Future[T]]): Future[List[T]] = {

      val result = Promise[List[T]]()
      result.success(Nil)

      fs.foldRight(result.future) {
        (f, acc) =>
          for { x <- f; xs <- acc }
            yield x :: xs
      }
    }

    /**
     * Given a list of futures `fs`, returns the future holding the value of the future from `fs` that completed first.
     *  If the first completing future in `fs` fails, then the result is failed as well.
     *
     *  E.g.:
     *
     *      Future.any(List(Future { 1 }, Future { 2 }, Future { throw new Exception }))
     *
     *  may return a `Future` succeeded with `1`, `2` or failed with an `Exception`.
     */
    def any[T](fs: List[Future[T]]): Future[T] = {

      val result = Promise[T]()

      var isReturned = false

      val lastIndex = fs.length - 1
      var i = 0

      for (i <- 0 to lastIndex) {

        fs(i) onComplete {
          case Failure(e) =>
            if (!isReturned) {
              result.failure(e)
              isReturned = true
            }
          case Success(x) =>
            if (!isReturned) {
              result.success(x)
              isReturned = true
            }
        }
      }
      result.future
    }

    /**
     * Returns a future with a unit value that is completed after time `t`.
     */
    def delay(t: Duration): Future[Unit] = {
      Future[Unit]({ blocking { Thread.sleep(t.toMillis) } })
    }

    /**
     * Completes this future with user input.
     */
    def userInput(message: String): Future[String] = Future {
      readLine(message)
    }

    /**
     * Creates a cancellable context for an execution and runs it.
     */
    def run()(f: CancellationToken => Future[Unit]): Subscription = {
      val s = CancellationTokenSource()
      f(s.cancellationToken)
      s
    }

  }

  /**
   * Adds extension methods to future objects.
   */
  implicit class FutureOps[T](val f: Future[T]) extends AnyVal {

    /**
     * Returns the result of this future if it is completed now.
     *  Otherwise, throws a `NoSuchElementException`.
     *
     *  Note: This method does not wait for the result.
     *  It is thus non-blocking.
     *  However, it is also non-deterministic -- it may throw or return a value
     *  depending on the current state of the `Future`.
     */
    def now: T = {
      val value = f.value

      if (value.exists(p => p.isSuccess))
        value.get.get
      else
        throw new NoSuchElementException
    }

    /**
     * Continues the computation of this future by taking the current future
     *  and mapping it into another future.
     *
     *  The function `cont` is called only after the current future completes.
     *  The resulting future contains a value returned by `cont`.
     */
    def continueWith[S](cont: Future[T] => S): Future[S] = {
      val p = Promise[S]

      f onComplete {
        case Failure(e) =>
          p.failure(e)
        case Success(x) =>
          val tryValue = Try(cont(f))
          if (tryValue.isFailure)
            p.failure(tryValue.failed.get)
          else
            p.success(tryValue.get)
      }

      p.future
    }

    /**
     * Continues the computation of this future by taking the result
     *  of the current future and mapping it into another future.
     *
     *  The function `cont` is called only after the current future completes.
     *  The resulting future contains a value returned by `cont`.
     */
    def continue[S](cont: Try[T] => S): Future[S] = {
      val p = Promise[S]

      f onComplete {
        case Failure(e) =>
          p.failure(e)
        case Success(x) =>
          val tryValue = Try(cont(Try(x)))
          if (tryValue.isFailure)
            p.failure(tryValue.failed.get)
          else
            p.success(tryValue.get)
      }

      p.future
    }
  }

  /**
   * Subscription objects are used to be able to unsubscribe
   *  from some event source.
   */
  trait Subscription {
    def unsubscribe(): Unit
  }

  object Subscription {
    /**
     * Given two subscriptions `s1` and `s2` returns a new composite subscription
     *  such that when the new composite subscription cancels both `s1` and `s2`
     *  when `unsubscribe` is called.
     */
    def apply(s1: Subscription, s2: Subscription) = new Subscription {
      def unsubscribe() {
        s1.unsubscribe()
        s2.unsubscribe()
      }
    }
  }

  /**
   * Used to check if cancellation was requested.
   */
  trait CancellationToken {
    def isCancelled: Boolean
    def nonCancelled = !isCancelled
  }

  /**
   * The `CancellationTokenSource` is a special kind of `Subscription` that
   *  returns a `cancellationToken` which is cancelled by calling `unsubscribe`.
   *
   *  After calling `unsubscribe` once, the associated `cancellationToken` will
   *  forever remain cancelled -- its `isCancelled` will return `false.
   */
  trait CancellationTokenSource extends Subscription {
    def cancellationToken: CancellationToken
  }

  /**
   * Creates cancellation token sources.
   */
  object CancellationTokenSource {
    /**
     * Creates a new `CancellationTokenSource`.
     */
    def apply(): CancellationTokenSource = new CancellationTokenSource {
      val p = Promise[Unit]()
      val cancellationToken = new CancellationToken {
        def isCancelled = p.future.value != None
      }
      def unsubscribe() {
        p.trySuccess(())
      }
    }
  }

}

