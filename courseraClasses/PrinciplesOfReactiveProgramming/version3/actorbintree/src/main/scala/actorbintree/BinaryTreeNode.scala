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



object BinaryTreeNode {
  trait Position

  case object Left extends Position
  case object Right extends Position

  case class CopyTo(treeNode: ActorRef)
  case object CopyFinished

  def props(elem: Int, initiallyRemoved: Boolean) = Props(classOf[BinaryTreeNode],  elem, initiallyRemoved)
}

class BinaryTreeNode(val elem: Int, initiallyRemoved: Boolean) extends Actor {
  import BinaryTreeNode._
  import BinaryTreeSet._

  implicit val timeout = Timeout(50000L, java.util.concurrent.TimeUnit.SECONDS)

  var subtrees = Map[Position, ActorRef]()
  var removed = initiallyRemoved

  // optional
  def receive = normal


  def insertRequest(insertCO : Insert) = {
    MyLogger.println1("insertRequest:" + insertCO)
    //Insert(requester: ActorRef, id: Int, elemIn: Int)
    if(insertCO.elem <= elem) {
      val leftTreeOp = subtrees get Left
      leftTreeOp match {
        case Some(ref) =>
          ref ! insertCO
        case None =>
          val leftTree = context.actorOf(BinaryTreeNode.props(insertCO.elem, initiallyRemoved = false))
          val rightTree = subtrees get Right
          rightTree match {
            case None => subtrees = Map(Left -> leftTree)
            case Some(ref) => subtrees = Map(Left -> leftTree, Right -> ref)
          }
          insertCO.requester ! OperationFinished(id=insertCO.id)
          MyLogger.println1(">>Insert Complete for:" + insertCO.elem)
      }
    } else {
      val rightTreeOp = subtrees get Right
      rightTreeOp match {
        case Some(ref) =>
          ref ! insertCO
        case None =>
          val rightTree = context.actorOf(BinaryTreeNode.props(insertCO.elem, initiallyRemoved = false))
          val leftTree = subtrees get Left
          leftTree match {
            case None => subtrees = Map(Right -> rightTree)
            case Some(ref) => subtrees = Map(Left -> ref, Right -> rightTree)
          }
          insertCO.requester ! OperationFinished(id=insertCO.id)
          MyLogger.println1(">>Insert Complete for:" + insertCO.elem)
      }
    }

  }
  /**
   *
   */
  // optional
  /** Handles `Operation` messages and `CopyTo` requests. */
  val normal: Receive = {
    case Print(requester: ActorRef, id: Int, elemIn: Int, str: String) =>
      val strNew = str + ":" + elem
      MyLogger.println1(strNew)
      subtrees get Left map {
        x =>
          x ! Print(requester, id, elem, strNew + ":" + "Left")
      }
      subtrees get Right map {
        x =>
          x ! Print(requester, id, elem, strNew + ":" + "Right")
      }
    case insertCO: Insert =>
      insertRequest(insertCO)
    case contains: Contains =>
      MyLogger.println1("Checking contains for node elem:" + elem)
      MyLogger.println1("Checking contains elem:" + contains.elem)
      doContainsTest(elem, contains)
    case Remove(requester: ActorRef, id: Int, elem: Int) =>
        doRemove(requester, id, elem)
    case CopyTo(treeNode: ActorRef) =>
      treeNode ? Insert(treeNode, 1, elem)
      val leftTreeOp = subtrees get Left
      leftTreeOp match {
        case Some(ref) =>
          val x = ref ?  CopyTo(treeNode)
        case _ =>
      }
      val rightTreeOp = subtrees get Right
      rightTreeOp match {
        case Some(ref) =>
          val x = ref ?  CopyTo(treeNode)
        case _ =>
      }

    case yy =>
      MyLogger.println1("Unexpected:" + yy)

  }

  def doRemove(requester: ActorRef, id: Int, elemIn: Int) = {
    if(elemIn == elem && !removed) {
      removed = true
      requester ! OperationFinished(id=id)
      MyLogger.println1(s">>Remove Complete for $elemIn")
    }

    subtrees get Left map {
      x =>
        x ! Remove(requester, id, elemIn)
    }
    subtrees get Right map {
      x =>
        x ! Remove(requester, id, elemIn)
    }

  }

  def doContainsTest(nodeElem: Int, contains: Contains)  = {
    implicit val timeout = Timeout(60L, java.util.concurrent.TimeUnit.SECONDS)

    if(contains.elem == nodeElem && !removed) {
      MyLogger.println1("Found match for value:" + elem)
      MyLogger.println1("Contains Requestor:" + contains.requester)
      MyLogger.println1("sender:" + sender)
      MyLogger.println1("contains.id:" + contains.id)
      sender ! ContainsResult(id = contains.id, true)
      MyLogger.println1(">>Contains Found positive for:" +  contains.elem)
    } else {
      var leftFound = false
      var rightFound = false
      subtrees get Left map {
        x =>
          MyLogger.println1("Found left tree")
          val future = x ? contains
          val tryVal = Try {Await.result(future, Duration.Inf)}
          tryVal match {
            case Success(x) if x == ContainsResult(contains.id, true)=>
              leftFound = true
              MyLogger.println1(">>Contains Found positive for:" +  contains.elem)
            case _ =>
          }
      }
      subtrees get Right map {
        x =>
          MyLogger.println1("Found right tree")
          val future = x ? contains
          val tryVal = Try{Await.result(future, Duration.Inf)}
          tryVal match {
            case Success(x) if x == ContainsResult(contains.id, true)=>
              rightFound = true
              MyLogger.println1(">>Contains Found positive for:" +  contains.elem)
            case _ =>
          }
      }
      if(leftFound || rightFound) {
        sender ! ContainsResult(id = contains.id, true)
      } else {
        sender ! ContainsResult(contains.id, false)
        MyLogger.println1(">>Contains Found negative for:" + contains.elem)
      }
    }
  }

  /**
  MyLogger.println1("Requestor:" + requester)
      MyLogger.println1("Initiated Contains Requestor:" + self)
      val future = root ? Contains(requester, id, elem)
      future onComplete {
        case Success(ContainsResult(id, true)) =>
          requester ! ContainsResult(id, true)
        case Success(ContainsResult(id, false)) =>
          requester ! ContainsResult(id, false)
        case Failure(e) =>
          requester ! ContainsResult(id, false)
      }
   */


  // optional
  /** `expected` is the set of ActorRefs whose replies we are waiting for,
    * `insertConfirmed` tracks whether the copy of this node to the new tree has been confirmed.
    */
  /*
  def copying(expected: Set[ActorRef], insertConfirmed: Boolean): Receive = {
    case _ =>
    //xxx
  }
  */



}

