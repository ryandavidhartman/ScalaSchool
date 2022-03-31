package nodescala

import scala.language.postfixOps
import scala.util.{ Try, Success, Failure }
import scala.collection._
import scala.concurrent._
import ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.async.Async.{ async, await }
import org.scalatest._
import NodeScala._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class NodeScalaSuite extends FunSuite {

  test("A Future should always be created") {
    val always = Future.always(517)

    assert(Await.result(always, 0 nanos) == 517)
  }

  test("A Future should never be created") {
    val never = Future.never[Int]

    try {
      Await.result(never, 1 second)
      assert(false)
    } catch {
      case t: TimeoutException => // ok!
    }
  }

  test("A Future should be completed with all the results") {

    val future1 = Future.always(123)
    val future2 = Future.always(555)
    val future3 = Future.always(456)

    val fs = List(future1, future2, future3)
    val all = Future.all(fs)

    assert(Await.result(all, 1 second) == List(123, 555, 456))
  }

  test("A Future should not be completed with all the results if at least one is faled") {

    val future1 = Future.always(123)
    val future2 = Future.never[Int]
    val future3 = Future.always(456)

    val fs = List(future1, future2, future3)
    val all = Future.all(fs)

    try {
      Await.result(all, 1 second)
      assert(false)
    } catch {
      case t: TimeoutException => // ok!
    }
  }

  test("A Future should be completed with any of the results") {

    val future1 = Future { 1 }
    val future2 = Future { 2 }
    val future3 = Future { 3 }

    val fs = List(future1, future2, future3)
    val any = Future.any(fs)

    val result = Await.result(any, 1 second)
    assert(result == 1 || result == 2 || result == 3)
  }

  test("A Future should fail with one of the exceptions when calling any") {

    val future1 = Future.never[Int]
    val future2 = Future { 1 }
    val future3 = Future { 2 }

    val fs = List(future1, future2, future3)
    val any = Future.all(fs)

    try {
      Await.result(any, 1 second)
      assert(false)
    } catch {
      case t: TimeoutException => // ok!
    }
  }

  test("A Future should complete after 3s when using a delay of 1s") {
    val delay = Future.delay(1 second)

    try {
      Await.result(delay, 3 second)
      assert(true)
    } catch {
      case t: TimeoutException => // ok!
    }
  }

  test("A Future should not complete after 1s when using a delay of 3s") {
    val delay = Future.delay(3 second)
    try {
      Await.result(delay, 1 second)
      assert(false)
    } catch {
      case t: TimeoutException => // ok!
    }
  }

  test("Future.now should return the result when completed") {
    val fo = Future.always(1)
    assert(fo.now == 1)
  }

  test("Future.now should throw a NoSuchElementException when not completed") {
    val fo = Future.never
    try {
      fo.now
      assert(false)
    } catch {
      case t: NoSuchElementException => // ok!
    }
  }

  test("Future.continueWith should contain the continued value") {
    val f = Future { 25 }
    val result = f.continueWith(FutureTToS)
    assert(Await.result(result, 1 second) == "25")
  }

  test(" Future.continueWith should handle exceptions thrown by the user specified continuation function") {
    val f = Future { 25 }
    val result = f.continueWith(FutureTToSWithException)
    try {
      Await.result(result, 1 second) == "25"
      assert(false)
    } catch {
      case t: NoSuchElementException => // ok!
    }
  }

  def FutureTToS(f: Future[Int]): String = {
    f.value.get.get.toString
  }

  def FutureTToSWithException(f: Future[Int]): String = {
    throw new NoSuchElementException
  }

  test("Future.continueW should contain the continued value") {
    val f = Future { 25 }
    val result = f.continue(TryTToS)
    assert(Await.result(result, 1 second) == "25")
  }

  test(" Future.continue should handle exceptions thrown by the user specified continuation function") {
    val f = Future { 25 }
    val result = f.continue(TryTToSWithException)
    try {
      Await.result(result, 1 second) == "25"
      assert(false)
    } catch {
      case t: NoSuchElementException => // ok!
    }
  }

  def TryTToS(t: Try[Int]): String = {
    t.get.toString
  }

  def TryTToSWithException(f: Try[Int]): String = {
    throw new NoSuchElementException
  }

  test("A Future should run until cancelled when using Future.run") {
    val p = Promise[String]()
    val working = Future.run() { ct =>
      Future {
       
        while (ct.nonCancelled) {
        }
        p.success("done")
      }
    }

    Future.delay(5 seconds) onSuccess {
      case _ => working.unsubscribe()
    }

    assert(Await.result(p.future, 7 second) == "done")
  }

  test("CancellationTokenSource should allow stopping the computation") {
    val cts = CancellationTokenSource()
    val ct = cts.cancellationToken
    val p = Promise[String]()

    async {
      while (ct.nonCancelled) {
        // do work
      }

      p.success("done")
    }

    cts.unsubscribe()
    assert(Await.result(p.future, 2 second) == "done")
  }

  class DummyExchange(val request: Request) extends Exchange {
    @volatile var response = ""
    val loaded = Promise[String]()
    def write(s: String) {
      response += s
    }
    def close() {
      loaded.success(response)
    }
  }

  class DummyListener(val port: Int, val relativePath: String) extends NodeScala.Listener {
    self =>

    @volatile private var started = false
    var handler: Exchange => Unit = null

    def createContext(h: Exchange => Unit) = this.synchronized {
      assert(started, "is server started?")
      handler = h
    }

    def removeContext() = this.synchronized {
      assert(started, "is server started?")
      handler = null
    }

    def start() = self.synchronized {
      started = true
      new Subscription {
        def unsubscribe() = self.synchronized {
          started = false
        }
      }
    }

    def emit(req: Request) = {
      val exchange = new DummyExchange(req)
      if (handler != null) handler(exchange)
      exchange
    }
  }

  class DummyServer(val port: Int) extends NodeScala {
    self =>
    val listeners = mutable.Map[String, DummyListener]()

    def createListener(relativePath: String) = {
      val l = new DummyListener(port, relativePath)
      listeners(relativePath) = l
      l
    }

    def emit(relativePath: String, req: Request) = this.synchronized {
      val l = listeners(relativePath)
      l.emit(req)
    }
  }

  test("Listener should serve the next request as a future") {
    val dummy = new DummyListener(8191, "/test")
    val subscription = dummy.start()

    def test(req: Request) {
      val f = dummy.nextRequest()
      dummy.emit(req)
      val (reqReturned, xchg) = Await.result(f, 1 second)

      assert(reqReturned == req)
    }

    test(immutable.Map("StrangeHeader" -> List("StrangeValue1")))
    test(immutable.Map("StrangeHeader" -> List("StrangeValue2")))

    subscription.unsubscribe()
  }

  test("Server should serve requests") {
    val dummy = new DummyServer(8191)
    val dummySubscription = dummy.start("/testDir") {
      request => for (kv <- request.iterator) yield (kv + "\n").toString
    }

    // wait until server is really installed
    Thread.sleep(500)

    def test(req: Request) {
      val webpage = dummy.emit("/testDir", req)
      val content = Await.result(webpage.loaded.future, 1 second)
      val expected = (for (kv <- req.iterator) yield (kv + "\n").toString).mkString
      assert(content == expected, s"'$content' vs. '$expected'")
    }

    test(immutable.Map("StrangeRequest" -> List("Does it work?")))
    test(immutable.Map("StrangeRequest" -> List("It works!")))
    test(immutable.Map("WorksForThree" -> List("Always works. Trust me.")))

    dummySubscription.unsubscribe()
  }

}




