package kvstore

import akka.actor.{Props, Actor, ActorSystem}
import akka.testkit.{ImplicitSender, TestKit, TestProbe}
import kvstore.Persistence.{Persisted, Persist}
import org.scalactic.ConversionCheckedTripleEquals
import org.scalatest.{BeforeAndAfterAll, FunSuiteLike, Matchers}
import scala.concurrent.duration.FiniteDuration
import scala.concurrent.duration._
import scala.language.postfixOps

class MyTest extends TestKit(ActorSystem("TestActorSystem"))
    with FunSuiteLike
        with BeforeAndAfterAll
    with Matchers
    with ConversionCheckedTripleEquals
    with ImplicitSender
    with Tools {

  override def afterAll(): Unit = {
    system.shutdown()
  }


  test("a test") {

    class MyActor extends Actor {
      var counter = 0
      def receive = {
        case "Counter" =>
          sender ! counter
        case _ =>
          counter += 1
          sender ! "OK"
      }
    }


    val props = Props(new MyActor())
    val ref = system.actorOf(props, "myactor")

    val persistenceRef = system.actorOf(MyPersist.props(flaky = true), "myPersist")

    val probe1 = TestProbe()
    probe1.send(ref, "Test")
    probe1.expectMsg("OK")
    probe1.send(ref, "Counter")
    probe1.expectMsg(1 seconds, 1)

    probe1.send(ref, "Test")
    probe1.expectMsg("OK")
    probe1.send(ref, "Counter")
    probe1.expectMsg(1 seconds, 2)

    /*
    probe1.send(persistenceRef, Persist("k1", Some("v1"), 10))
    probe1.expectMsg(Persisted("k1", 10))
  */
  }



  
}
