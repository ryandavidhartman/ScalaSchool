/**
 * Copyright (C) 2009-2013 Typesafe Inc. <http://www.typesafe.com>
 */
package actorbintree

import akka.actor._
import akka.pattern.{ ask, pipe }
import scala.collection.immutable.Queue
import scala.concurrent._
import scala.util.Success
import scala.util.Failure
import ExecutionContext.Implicits.global
import scala.util.Success
import scala.util.Success

object BinaryTreeSet {

  trait Operation {
    def requester: ActorRef
    def id: Int
    def elem: Int
  }

  trait OperationReply {
    def id: Int
  }

  /**
   * Request with identifier `id` to insert an element `elem` into the tree.
   * The actor at reference `requester` should be notified when this operation
   * is completed.
   */
  case class Insert(requester: ActorRef, id: Int, elem: Int) extends Operation

  /**
   * Request with identifier `id` to check whether an element `elem` is present
   * in the tree. The actor at reference `requester` should be notified when
   * this operation is completed.
   */
  case class Contains(requester: ActorRef, id: Int, elem: Int) extends Operation

  /**
   * Request with identifier `id` to remove the element `elem` from the tree.
   * The actor at reference `requester` should be notified when this operation
   * is completed.
   */
  case class Remove(requester: ActorRef, id: Int, elem: Int) extends Operation

  /** Request to perform garbage collection*/
  case object GC

  /**
   * Holds the answer to the Contains request with identifier `id`.
   * `result` is true if and only if the element is present in the tree.
   */
  case class ContainsResult(id: Int, result: Boolean) extends OperationReply

  /** Message to signal successful completion of an insert or remove operation. */
  case class OperationFinished(id: Int) extends OperationReply
}

class BinaryTreeSet extends Actor {
  import BinaryTreeSet._
  import BinaryTreeNode._

  def createRoot: ActorRef = context.actorOf(BinaryTreeNode.props(0, initiallyRemoved = false))

  var root = createRoot

  // optional
  var pendingQueue = Queue.empty[Operation]

  // optional
  def receive = normal

  val withQueue: Receive = {
    case Insert(requester, id, elem) => {

      pendingQueue ++= Queue(Insert(requester, id, elem))

      root ! InsertNode(requester, id, elem)
    }
    case Remove(requester, id, elem) => {
      println("remove", requester, id, elem)

    }
    case Contains(requester, id, elem) => {

      requester ! ContainsResult(id, false)
    }
    case GC => println("GC")
    case _ => println("another")
  }

  // optional
  /** Accepts `Operation` and `GC` messages. */
  val normal: Receive =
    {
      case Insert(requester, id, elem) => {
        root ! InsertNode(requester, id, elem)
      }
      case Remove(requester, id, elem) => {
        root ! RemoveNode(requester, id, elem)
      }
      case Contains(requester, id, elem) => {
        val f = ask(root, ContainsNode(elem))(10000).mapTo[Boolean]
        f onComplete {
          case Success(x) => requester ! ContainsResult(id, x)
          case Failure(err) => {
            println("error contains", err)
            requester ! ContainsResult(id, false)
          }
        }
      }
      case GC => {
        val newRoot = context.actorOf(BinaryTreeNode.props(0, initiallyRemoved = false))
        val f = ask(root, CopyTo(newRoot))(10000).mapTo[BinaryTreeNode]
        f onComplete {
          case Success(x) => {
            println("copied!!!")
            context.stop(root)
            root = newRoot
          }
          case Failure(err) => println("Error while copy", err)
        }

      }
      case _ => println("another")
    }

  // optional
  /**
   * Handles messages while garbage collection is performed.
   * `newRoot` is the root of the new binary tree where we want to copy
   * all non-removed elements into.
   */
  def garbageCollecting(newRoot: ActorRef): Receive = ???

}

object BinaryTreeNode {
  trait Position

  case object Left extends Position
  case object Right extends Position

  case class CopyTo(treeNode: ActorRef)
  case object CopyFinished

  case class InsertNode(requester: ActorRef, id: Int, elem: Int)
  case class RemoveNode(requester: ActorRef, id: Int, elem: Int)

  case class ContainsNode(elem: Int)

  def props(elem: Int, initiallyRemoved: Boolean) = Props(classOf[BinaryTreeNode], elem, initiallyRemoved)
}

class BinaryTreeNode(val elem: Int, initiallyRemoved: Boolean) extends Actor {
  import BinaryTreeNode._
  import BinaryTreeSet._

  var subtrees = Map[Position, ActorRef]()
  var removed = initiallyRemoved

  // optional
  def receive = normal

  // optional
  /** Handles `Operation` messages and `CopyTo` requests. */
  val normal: Receive = {
    case CopyTo(treeNode) =>
      {
        println("copyTo", treeNode)
        println("copy from root", context.parent)

        if (!removed)
          self ! InsertNode(treeNode, 1, elem)

        copyFromPosition(treeNode, Right)
        copyFromPosition(treeNode, Left)

        sender ! treeNode
      }
    case CopyFinished => println("copy finished")
    case ContainsNode(element) => {
      if (element == elem) sendContainsResult(sender, !removed)
      if (element > elem) containsInPosition(sender, element, Right)
      if (element < elem) containsInPosition(sender, element, Left)
    }
    case InsertNode(requester, id, element) => {
      if (element == elem) {
        if (removed) removed = false
        finish(requester, id)
      }

      if (element > elem) insertToPosition(requester, id, element, Right)
      if (element < elem) insertToPosition(requester, id, element, Left)
    }
    case RemoveNode(requester, id, element) => {
      if (element == elem) {
        if (!removed) removed = true
        finish(requester, id)
      }

      if (element > elem) removeFromPosition(requester, id, element, Right)
      if (element < elem) removeFromPosition(requester, id, element, Left)
    }
    case _ => println("node unknown")
  }

  def finish(requester: ActorRef, id: Int) = {
    requester ! OperationFinished(id)
  }

  def sendContainsResult(sender: ActorRef, result: Boolean) {
    sender ! result
  }

  // optional
  /**
   * `expected` is the set of ActorRefs whose replies we are waiting for,
   * `insertConfirmed` tracks whether the copy of this node to the new tree has been confirmed.
   */
  def copying(expected: Set[ActorRef], insertConfirmed: Boolean): Receive = ???

  def insertToPosition(requester: ActorRef, id: Int, element: Int, pos: Position) = {
    if (subtrees contains pos)
      subtrees(pos) ! InsertNode(requester, id, element)
    else {
      subtrees += pos -> context.actorOf(BinaryTreeNode.props(element, initiallyRemoved = false))
      finish(requester, id)
    }
  }

  def removeFromPosition(requester: ActorRef, id: Int, element: Int, pos: Position) = {
    if (subtrees contains pos)
      subtrees(pos) ! RemoveNode(requester, id, element)
    else {
      finish(requester, id)
    }
  }

  def copyFromPosition(treeNode: ActorRef, position: Position) = {
    if (subtrees contains position)
      subtrees(position) ! CopyTo(treeNode)
  }

  def containsInPosition(sender: ActorRef, element: Int, pos: Position) = {
    if (subtrees contains pos) {
      val f = ask(subtrees(pos), ContainsNode(element))(10000).mapTo[Boolean]
      f onComplete {
        case Success(x) => sendContainsResult(sender, x)
        case Failure(err) => {
          println("containsInPosition error", element, err)
          sendContainsResult(sender, false)
        }
      }
    } else
      sendContainsResult(sender, false)
  }
}