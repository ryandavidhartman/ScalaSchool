package lectures.polymorphism

import java.util.NoSuchElementException


trait List[T] {
  def isEmpty: Boolean
  def head: T
  def tail: List[T]
}


class Cons[T](val head:T, val tail: List[T]) extends List[T] {
  def isEmpty: Boolean = false
}


class Nil[T]  extends List[T] {
  def isEmpty: Boolean = true

  def tail: Nothing = throw new NoSuchElementException("Nil.tail")

  def head: Nothing = throw new NoSuchElementException("Nil.head")
}

object helpers {

  def singelton[T](elem: T): List[T] = new Cons[T](elem, new Nil[T])

  def nth[T](n: Int, list: List[T]): T = {
    if(n == 0 && !list.isEmpty) list.head
    else if(list.isEmpty || n < 0) throw new IndexOutOfBoundsException("oops!")
    else nth(n-1,list.tail)
  }
}

object List {
  def apply[T]() = new Nil[T]
  def apply[T](x: T): List[T] = new Cons(x, new Nil)
  def apply[T](x1: T, x2: T): List[T] = new Cons(x1, new Cons(x2, new Nil))
}




