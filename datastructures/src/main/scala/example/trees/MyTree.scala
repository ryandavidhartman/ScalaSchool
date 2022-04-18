package example.trees

import scala.annotation.tailrec

// Simple BST

sealed trait MyTree[+T] {
  @tailrec
  final def apply[U >: T](data: Seq[U], acc: MyTree[U] = Leaf)(implicit ordering: Ordering[U]): MyTree[U] = data match {
    case Nil => acc
    case xs => apply(xs.tail, acc.insert(xs.head))
  }

  def insert[U >: T](data: U)(implicit ordering: Ordering[U]): MyTree[U] = this match {
    case Leaf => Node(data, Leaf, Leaf)
    case Node(n, l, r) =>
      if(ordering.eq(data, n))
        this
      else if (ordering.gt(data, n))
        Node(n, l, r.insert(data))
      else
        Node(n, l.insert(data), r)
  }

  def foreach(f: T => Unit): Unit
}

case object Leaf extends MyTree[Nothing] {
  override def foreach(f: Nothing => Unit): Unit = ()
}

case class Node[+T](node: T, left: MyTree[T], right: MyTree[T]) extends MyTree[T] {
  override def foreach(f: T => Unit): Unit = {
    f(node)
    left.foreach(f)
    right.foreach(f)
  }
}



