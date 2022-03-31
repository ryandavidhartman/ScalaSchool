/**
 * Copyright (C) 2009-2013 Typesafe Inc. <http://www.typesafe.com>
 */
package actorbintree

import common.MyLogger
import akka.actor._
import akka.util._
import scala.collection.immutable.Queue
import akka.pattern.{ ask, pipe }
import scala.concurrent.duration._

import scala.language.postfixOps
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.Await
import scala.util._


object BinaryTreeSet {

  trait Operation {
    def requester: ActorRef
    def id: Int
    def elem: Int
  }

  trait OperationReply {
    def id: Int
  }

  /** Request with identifier `id` to insert an element `elem` into the tree.
    * The actor at reference `requester` should be notified when this operation
    * is completed.
    */
  case class Insert(requester: ActorRef, id: Int, elem: Int) extends Operation

  /** Request with identifier `id` to check whether an element `elem` is present
    * in the tree. The actor at reference `requester` should be notified when
    * this operation is completed.
    */
  case class Contains(requester: ActorRef, id: Int, elem: Int) extends Operation

  /** Request with identifier `id` to remove the element `elem` from the tree.
    * The actor at reference `requester` should be notified when this operation
    * is completed.
    */
  case class Remove(requester: ActorRef, id: Int, elem: Int) extends Operation

  /** Request to perform garbage collection*/
  case object GC

  case class Print(requester: ActorRef, id: Int, elem: Int, str: String) extends Operation

  /** Holds the answer to the Contains request with identifier `id`.
    * `result` is true if and only if the element is present in the tree.
    */
  case class ContainsResult(id: Int, result: Boolean) extends OperationReply
  
  /** Message to signal successful completion of an insert or remove operation. */
  case class OperationFinished(id: Int) extends OperationReply

}


class BinaryTreeSet extends Actor {

  import BinaryTreeSet._
  import BinaryTreeNode._

  implicit val timeout = Timeout(60L, java.util.concurrent.TimeUnit.SECONDS)

  def createRoot: ActorRef = context.actorOf(BinaryTreeNode.props(0, initiallyRemoved = true))

  var root = createRoot

  // optional
  var pendingQueue = Queue.empty[Operation]

  // optional
  def receive = normal


  //Await at the Actor for BinaryTreeSet
  //Thread.sleep(10000)
  //val b = future.isCompleted
  //MyLogger.println1(s"Is Completed:$b")

  // optional
  /** Accepts `Operation` and `GC` messages. */
  val normal: Receive = {
    case Print(requester: ActorRef, id: Int, elem: Int, str: String) =>
      root ! Print(requester, id, elem, str)
    case Insert(requester: ActorRef, id: Int, elem: Int) =>
      MyLogger.println1("Start Insert:" + Insert(requester: ActorRef, id: Int, elem: Int))
      root ! Insert(requester, id, elem)

    case Contains(requester: ActorRef, id: Int, elem: Int) =>
      val contains = containsElement(requester, id, elem)
      if(contains)
        requester ! ContainsResult(id, true)
      else
        requester ! ContainsResult(id, false)

    case Remove(requester: ActorRef, id: Int, elem: Int) =>
      val contains = containsElement(requester, id, elem)
      if(!contains) {
        requester ! OperationFinished(id)
      }
      root ! Remove(requester, id, elem)

    case GC =>
      MyLogger.println1("GC requested")
      val newRoot = createRoot
      context.become(garbageCollecting(newRoot))

  }

  def containsElement(requester: ActorRef, id: Int, elem: Int) : Boolean = {
    MyLogger.println1("Requestor:" + requester)
    MyLogger.println1("Initiated Contains Requestor:" + self)
    val future = root ? Contains(requester, id, elem)
    val tryVal = Try{Await.result(future, Duration.Inf)}
    tryVal match {
      case Success(x) if(x == ContainsResult(id, true))=>
        MyLogger.println1("Success id=" + id)
        true
      case xxx =>
        MyLogger.println1("No match id=" + id + "result=" + xxx)
        false
    }
    /**
    MyLogger.println1("Requestor:" + requester)
    MyLogger.println1("Initiated Contains Requestor:" + self)
    val future = root ? Contains(requester, id, elem)
    val tryVal = Try{Await.result(future, Duration.Inf)}
    tryVal match {
      case Success(x) if(x == ContainsResult(id, true))=>
        MyLogger.println1("Success id=" + id)
        requester ! ContainsResult(id, true)
      case xxx =>
        MyLogger.println1("No match id=" + id + "result=" + xxx)
        requester ! ContainsResult(id, false)
    }
      **/
  }
  // optional
  /** Handles messages while garbage collection is performed.
    * `newRoot` is the root of the new binary tree where we want to copy
    * all non-removed elements into.
    */
  def garbageCollecting(newRoot: ActorRef): Receive = {
    case operation: Operation =>
      pendingQueue enqueue operation
    case GC =>
      root ? CopyTo(newRoot)
      context.stop(root)
      root = newRoot
      context.unbecome()
      pendingQueue foreach {
        x =>
          self ! x
      }
  }

}

