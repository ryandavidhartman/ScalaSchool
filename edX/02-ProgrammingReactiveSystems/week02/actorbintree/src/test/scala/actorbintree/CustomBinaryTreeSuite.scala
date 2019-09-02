package actorbintree

import akka.actor.{Actor, ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestKit, TestProbe}

import scala.concurrent.duration._

object CustomBinaryTreeSuite extends App {
  import BinaryTreeSet._

  implicit val system:ActorSystem = ActorSystem("CustomTestSys")

  val binaryTreeSet = system.actorOf(Props[BinaryTreeSet])
  val p = TestProbe()

  p.send(binaryTreeSet, msg = Insert(p.testActor, 1, 101) )
  p.expectMsg(OperationFinished(id = 1))

  p.send(binaryTreeSet, msg = Contains(p.testActor, 2, 101))
  p.expectMsg(ContainsResult(id = 2, result = true))

  p.send(binaryTreeSet, msg = Contains(p.testActor, 3, 103))
  p.expectMsg(ContainsResult(id = 3, result = false))

  p.send(binaryTreeSet, msg = Insert(p.testActor, 4, 99) )
  p.expectMsg(OperationFinished(id = 4))

  p.send(binaryTreeSet, msg = Insert(p.testActor, 5, 1000) )
  p.expectMsg(OperationFinished(id = 5))

  p.send(binaryTreeSet, msg = Contains(p.testActor, 6, 99))
  p.expectMsg(ContainsResult(id = 6, result = true))

  p.send(binaryTreeSet, msg = Contains(p.testActor, 7, 1000))
  p.expectMsg(ContainsResult(id = 7, result = true))

  p.send(binaryTreeSet, msg = Remove(p.testActor, 8, 99))
  p.expectMsg(OperationFinished(id = 8))

  p.send(binaryTreeSet, msg = Contains(p.testActor, 9, 99))
  p.expectMsg(ContainsResult(id = 9, result = false))

  system.terminate()

}
