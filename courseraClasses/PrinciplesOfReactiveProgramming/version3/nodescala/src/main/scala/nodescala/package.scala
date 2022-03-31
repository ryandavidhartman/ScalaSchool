import java.util
import java.util.concurrent.LinkedBlockingQueue

import scala.language.postfixOps
import scala.io.StdIn
import scala.util._
import scala.util.control.NonFatal
import scala.concurrent._
import scala.concurrent.duration._
import ExecutionContext.Implicits.global
import scala.async.Async.{async, await}

/** Contains basic data types, data structures and `Future` extensions.
 */
package object nodescala {

  /** Adds extensions methods to the `Future` companion object.
   */
  implicit class FutureCompanionOps(val f: Future.type) extends AnyVal {

    /** Returns a future that is always completed with `value`.
     */
    def always[T](value: T): Future[T] = Future {
      value
    }
    /** Returns a future that is never completed.
     *
     *  This future may be useful when testing if timeout logic works correctly.
     */
    def never[T]: Future[T] = Future {
      val q = new LinkedBlockingQueue[T]()
      val z = q.take()
      z
    }

    /** Given a list of futures `fs`, returns the future holding the list of values of all the futures from `fs`.
     *  The returned future is completed only once all of the futures in `fs` have been completed.
     *  The values in the list are in the same order as corresponding futures `fs`.
     *  If any of the futures `fs` fails, the resulting future also fails.
     */
    def all[T](fs: List[Future[T]]): Future[List[T]] = {

      val promise = Promise[List[T]]()
      val resultList = new util.ArrayList[T]()

      val resultQueue = new LinkedBlockingQueue[(Int,T)]()


      def getResult() = {
        var result: List[(Int,T)] = Nil

        while(!resultQueue.isEmpty) {
          val x: (Int,T) = resultQueue.take()
          result = x :: result
        }
        result
      }
      var i =0
      for (fut <- fs) {
        val a = i
        fut.onComplete {
          case Success(s) =>
            println("Remember Index:"+ a)
            resultQueue.put((a,s))
            if(resultQueue.size() == fs.size) {
              val res = getResult()
              val res2 = res.sortWith((x,y) => if (x._1 < y._1) true else false)
              val res3 = res2.map(x => x._2)
              promise.trySuccess(res3)
            }
          case Failure(f) =>
            println("Found exception: "+ f)
            promise.tryFailure(f)
        }
        i += 1
      }

      promise.future

    }
    /** Given a list of futures `fs`, returns the future holding the value of the future from `fs` that completed first.
     *  If the first completing future in `fs` fails, then the result is failed as well.
     *
     *  E.g.:
     *
     *      Future.any(List(Future { 1 }, Future { 2 }, Future { throw new Exception }))
     *
     *  may return a `Future` succeeded with `1`, `2` or failed with an `Exception`.
     */
    def any[T](fs: List[Future[T]]): Future[T] = {


      val promise = Promise[T]()

      for (fut <- fs) {
        fut.onComplete {
          case Success(s) =>
            promise.trySuccess(s)
          case Failure(f) =>
            println("Found exception: "+ f)
            promise.tryFailure(f)
        }
      }

      promise.future

    }

    /** Returns a future with a unit value that is completed after time `t`.
     */
    def delay(t: Duration): Future[Unit] = {
      //Await.result(Future { Thread.sleep(t.toMillis * 2)}, t)
      Future {
        Thread.sleep(t.toMillis * 2)
        ()
      }
    }

    /** Completes this future with user input.
     */
    def userInput(message: String): Future[String] = Future {
      blocking {
        StdIn.readLine(message)
      }
    }

    /** Creates a cancellable context for an execution and runs it.
     */
    def run()(f: CancellationToken => Future[Unit]): Subscription = new Subscription() {
      override def unsubscribe(): Unit = {
        val cts = CancellationTokenSource()
        val cancellationToken = cts.cancellationToken
        val future = f(cancellationToken)
        future.onComplete {
          case Success(_) =>
            cts.unsubscribe()
          case Failure(throwable) =>
            ()
        }

      }
    }

  }

  /** Adds extension methods to future objects.
   */
  implicit class FutureOps[T](val f: Future[T]) extends AnyVal {

    /** Returns the result of this future if it is completed now.
     *  Otherwise, throws a `NoSuchElementException`.
     *
     *  Note: This method does not wait for the result.
     *  It is thus non-blocking.
     *  However, it is also non-deterministic -- it may throw or return a value
     *  depending on the current state of the `Future`.
     */
    def now: T =  {

      f.value match {
        case Some(x) =>
          x match {
            case Success(s) =>
              s
            case Failure(f) =>
              throw new NoSuchElementException()
          }
        case None =>
          throw new NoSuchElementException()
      }

    }

    /** Continues the computation of this future by taking the current future
     *  and mapping it into another future.
     *
     *  The function `cont` is called only after the current future completes.
     *  The resulting future contains a value returned by `cont`.
     */
    /*
    def continueWith[S](cont: Future[T] => S): Future[S] = {
      val promise = Promise[S]
      f.onComplete {
        case Success(_) =>
          try {
            val sres = cont(f)
            promise.success(sres)
          } catch {
            case t: Throwable =>
              promise.failure(t)
            case t: TimeoutException =>
              promise.failure(t)

          }
        case Failure(throwable) =>
          promise.failure(throwable)
      }

      promise.future

    }
    */

    /*
    def continueWith[S](cont: Future[T] => S): Future[S] = {


      f map { _ => cont(f)}


    }
    */

    def continueWith[S](cont: Future[T] => S): Future[S] = {
      val promise = Promise[S]
      f onComplete  {
        case _ =>
            try {
              val x = cont(f)
              promise.success(x)
            } catch {
              case t: Throwable  => promise.failure(t)
            }
      }

      promise.future
    }



    /** Continues the computation of this future by taking the result
     *  of the current future and mapping it into another future.
     *
     *  The function `cont` is called only after the current future completes.
     *  The resulting future contains a value returned by `cont`.
     */
    def continue[S](cont: Try[T] => S): Future[S] = {
      val promise = Promise[S]
      f onComplete {
         x =>
            try {
              val y = cont(x)
              promise.success(y)
            } catch {
              case t: Throwable =>
                promise.failure(t)
            }
      }
      promise.future
      /*
      {
        cont

        case success : Success[T] =>
          promise.success(cont(success))
        case f : Failure =>
          promise.success(cont(f))
        case t : Try =>
          promi

      }
*/
      //promise.future

    }

  }

  /** Subscription objects are used to be able to unsubscribe
   *  from some event source.
   */
  trait Subscription {
    def unsubscribe(): Unit
  }

  object Subscription {
    /** Given two subscriptions `s1` and `s2` returns a new composite subscription
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

  /** Used to check if cancellation was requested.
   */
  trait CancellationToken {
    def isCancelled: Boolean
    def nonCancelled = !isCancelled
  }

  /** The `CancellationTokenSource` is a special kind of `Subscription` that
   *  returns a `cancellationToken` which is cancelled by calling `unsubscribe`.
   *
   *  After calling `unsubscribe` once, the associated `cancellationToken` will
   *  forever remain cancelled -- its `isCancelled` will return `false.
   */
  trait CancellationTokenSource extends Subscription {
    def cancellationToken: CancellationToken
  }

  /** Creates cancellation token sources.
   */
  object CancellationTokenSource {
    /** Creates a new `CancellationTokenSource`.
     */
    def apply() = new CancellationTokenSource {
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

