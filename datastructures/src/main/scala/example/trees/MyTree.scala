package example.trees

import scala.annotation.tailrec

// Simple BST

sealed trait MyTree[+T] {
  def insert[U >: T](datae: U)(implicit ordering: Ordering[U]): MyTree[U]

  def isEmpty(): Boolean
  def contains[U >: T](element: U)(implicit ordering: Ordering[U]): Boolean

  def foreach(f: T => Unit): Unit
}

case object Leaf extends MyTree[Nothing] {
  override def insert[U >: Nothing](data: U)(implicit ordering: Ordering[U]): MyTree[U] =
    Node(data, Leaf, Leaf)

  override def isEmpty(): Boolean = true
  override def contains[U >: Nothing](element: U)(implicit ordering: Ordering[U]): Boolean = false

  override def foreach(f: Nothing => Unit): Unit = ()

}

case class Node[+T](node: T, left: MyTree[T], right: MyTree[T]) extends MyTree[T] {

  override def insert[U >: T](data: U)(implicit ordering: Ordering[U]): MyTree[U] = {
    if(ordering.eq(data, node))
      this
    else if (ordering.gt(data, node))
      Node(node, left, right.insert(data))
    else
      Node(node, left.insert(data), right)
  }

  override def isEmpty(): Boolean = false
  override def contains[U >: T](element: U)(implicit ordering: Ordering[U]): Boolean = {
    if(element == node)
      true
    else if(ordering.gt(node, element))
      left.contains(element)
    else
      right.contains(element)
  }

  override def foreach(f: T => Unit): Unit = {
    f(node)
    left.foreach(f)
    right.foreach(f)
  }
}

case object MyTree {

  def apply[T](d: T*)(implicit ordering: Ordering[T]): MyTree[T] = apply(d, Leaf)(ordering)

  def apply[T](data: Seq[T], acc: MyTree[T] = Leaf)(implicit ordering: Ordering[T]): MyTree[T] = data match {
    case Nil => acc
    case xs => apply(xs.tail, acc.insert(xs.head))
  }
}



