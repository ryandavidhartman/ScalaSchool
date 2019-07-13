package week4
import java.util.NoSuchElementException

trait MyList[+T] {
  def isEmpty: Boolean
  def head: T
  def tail: MyList[T]
  def prepend [U >: T] (elem: U) : MyList[U] = new Cons(elem, this)
}

class Cons[T](val head: T, val tail: MyList[T]) extends MyList[T] {

  def isEmpty: Boolean = false

  override  def toString = if(tail.isEmpty) head.toString
  else head.toString + ", " + tail.toString
}

object MyNil extends MyList[Nothing] {
  def isEmpty: Boolean = true
  def head: Nothing = throw new NoSuchElementException("Nil.head")
  def tail: Nothing = throw new NoSuchElementException("Nil.tail")

}

object MyList {
  def apply[T] : MyList[T] = MyNil
  def apply[T](x:T) : MyList[T] = new Cons(x, apply)
  def apply[T](x: T, y: T) : MyList[T] = new Cons(x, apply(y))
  def apply[T](x:T, y:T, z:T) : MyList[T] = new Cons(x, apply(y,z))
}










