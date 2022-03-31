package nodescala

import scala.language.postfixOps
import scala.util.{Try, Success, Failure}
import scala.collection._
import scala.concurrent._
import ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.async.Async.{async, await}
import org.scalatest._
import NodeScala._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class NodeScalaSuite extends FunSuite {

  test("A Future should always be completed") {
    val always = Future.always(517)

    always.onSuccess { case x => println("Result=" + x)}
    assert(Await.result(always, 0 nanos) == 517)
  }
  test("A Future should never be completed") {
    val never = Future.never[Int]

    try {
      val x = Await.result(never, 1 second)
      assert(false)
    } catch {
      case t: TimeoutException =>
        println("There was a timeout")// ok!
    }
  }

  test("The All test 1") {
    val all = Future.all(List(Future{Thread.sleep(10000); 1}, Future{1/0}))

    all.onComplete {
      case Success(lst :List[Int]) =>
        println("Success for all function")
        assert(false)
      case Failure(e: Throwable) =>
        println("Failure for all function")
        assert(true, "Must see exception in this test")
      case _ =>
        println("Unexpected case")
        assert(false, "Future on complete can be Success or Failure. Not anything else")
    }

  }

  test("The All test 2") {
    val all = Future.all(List(Future{1}, Future{Thread.sleep(10000); 1/0}))

    all.onComplete {
      case Success(lst :List[Int]) =>
        println("Success for all function")
        assert(false)
      case Failure(e: Throwable) =>
        println("Failure for all function")
        assert(true, "Must see exception in this test")
      case _ =>
        println("Unexpected case")
        assert(false, "Future on complete can be Success or Failure. Not anything else")
    }

  }



  test("The All test 3") {
    val all = Future.all(List(Future{Thread.sleep(1000); 1}, Future{2}, Future{3}))

    all.onComplete {
      case Success(lst :List[Int]) =>
        println("Success for all function")
        lst == List(1,2, 3)
        assert(true, "Resulting list must conatin the values 1,2,3")
      case Failure(e: Throwable) =>
        println("Failure for all function")
        assert(false, "Must see exception in this test")
      case _ =>
        println("Unexpected case")
        assert(false, "Future on complete can be Success or Failure. Not anything else")
    }

    //Thread.sleep(10000000)

  }

  test("xcontinueWith should wait for the first future to complete") {
    val delay = Future.delay(1 second)
    val always = (f: Future[Unit]) => 42

    try {
      Await.result(delay.continueWith(always), 500 millis)
      assert(false)
    }
    catch {
      case t: TimeoutException => // ok
    }

    Thread.sleep(5000)
  }

  test("continueWith simple2") {

    val cont = (fT : Future[Int]) => {

      val x = Await.result(fT, 2 seconds)
      (x+1).toString

    }

    val s = Promise[Int]().complete(Success(3)).future.continueWith(cont)

    s.onComplete {
      case Success(x) => assert(x equals "4")
      case Failure(exn) => fail(exn)
    }

    Thread.sleep(10000)

  }

  test("continueWith simple")
  {

    Future { 3 }
      .continueWith(fT => (Await.result(fT, 1 seconds) + 1).toString)
      .onComplete {
      case Success(x) => assert(x equals "4")
      case Failure(exn) => fail(exn)
    }

    Thread.sleep(10000)
  }

  test("continueWith should wait for the first future to complete") {
    val delay = Future.delay(1 second)
    val always = (f: Future[Unit]) => 42

    try {
      Await.result(delay.continueWith(always), 500 millis)
      assert(false)
    }
    catch {
      case t: TimeoutException => // ok
    }

    Thread.sleep(10000)

  }

  test("continueWith") {
    val future = Future {
      Thread.sleep(1000 * 10); 111
    }
    val fu = Future {2}


    val res = future.continueWith(fu => {
      "testme"
    })

    res.onComplete(
    {
      case Success(successValue) =>
        assert(successValue == "testme", "Incorrect value produced by continuewith")
      case Failure(e) =>
        assert(true)
      case _ =>
        println("Cannot come here")
    }
    )

    Thread.sleep(1000 * 20)
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

  test("Promise") {

    try {
      val p = Promise[Unit]()
      p.failure(new Exception("Hah ha "))
      p.complete(Try {()})
      assert(false)
    } catch {
      case ill: IllegalStateException
      => assert(true)
      case _ : Throwable => assert(false)
    }

    try {
      val p = Promise[Unit]()
      p.complete(Try {()})
      p.failure(new Exception("Hah ha "))
      assert(false)
    } catch {
      case ill: IllegalStateException
      => assert(true)
      case _ : Throwable => assert(false)
    }

    try {
      val p = Promise[Unit]()
      p.failure(new Exception("Hah ha "))
      assert(true)
    } catch {
      case _ : Throwable => assert(false)
    }

    try {
      val p = Promise[Unit]()
      p.complete(Try {()})
      assert(true)
    } catch {
      case _ : Throwable => assert(false)
    }

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




