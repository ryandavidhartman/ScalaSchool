package week04.variance

trait List[+T] {
  def isEmpty: Boolean
  def head: T
  def tail: List[T]
  def prepend[U >: T](elem: U): List[U] = new Cons[U](elem, this)
}

class Cons[T](val head: T, val tail: List[T]) extends List[T] {
  def isEmpty = false
}

object Nil extends List[Nothing] {
  def isEmpty: Boolean = true
  def head: Nothing = throw new NoSuchElementException("Nil.head")
  def tail: Nothing = throw new NoSuchElementException("Nil.tail")
}

object test {
  val x: List[String] = Nil
  def bob(xs: List[NonEmpty2], x: Empty2) = xs prepend x
}

abstract class IntSet2 {
  def incl(x: Int): IntSet2
  def contains(x:Int): Boolean
  def union(other: IntSet2): IntSet2
}

/**
  * In the original IntSet example, one could argue that there is really only a
  * single empty IntSet.
  *
  * So it seems overkill to have the user crate many instances of it.
  * We can express this case better with an object definition.
  *
  * This defines a singelton object named Empty.
  * No other Empty instances can be (or need to be) created.
  * Singleton objects are values so Empty evaluates to itself.
  */

class Empty2 extends IntSet2 {
  def incl(x: Int): IntSet2 = new NonEmpty2(x, new Empty2, new Empty2)

  def contains(x: Int): Boolean = false

  override def union(other: IntSet2): IntSet2 = other

  override def toString = "."

}


case class NonEmpty2(elem: Int, left: IntSet2, right: IntSet2) extends IntSet2 {
  def incl(x: Int): IntSet2 =
    if(x < elem) new NonEmpty2(elem, left incl x, right)
    else if (x > elem) new NonEmpty2(elem, left, right incl x)
    else this

  def contains(x: Int): Boolean =
    if(x < elem) left contains x
    else if(x > elem) right contains x
    else true

  override def union(other: IntSet2): IntSet2 = ((left union right) union other) incl elem
  override def toString = s"{ ${left} $elem ${right} }"
}






