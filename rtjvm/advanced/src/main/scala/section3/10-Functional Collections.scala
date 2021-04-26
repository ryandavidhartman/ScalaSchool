package section3

import scala.annotation.tailrec

object FunctionalCollections extends App {

  val set = Set(1, 2, 3)  // An instance of the Set class from the standard Scala collections library

  // Since Set has an apply method, we can "call" a set.  Here the apply method returns a boolean
  // describing whether or not the value of the input parameter is in the set.

  println(s"Is 0 in the set? ${set(0)},  Is 1 in the set? ${set(1)}")

  // The point is a Set is callable like a function!  So if a collection is callable like a function,
  // why not actually define a collection in terms of a function?

  // BTW is actually is how Sets are defined in Scala.  trait Set[A] extends (A) => Boolean with {other stuff}
  // So the actual type definition of a set in Scala is a function type.

  // Lets play around with this ourselves.  Exercise Functional Set
  //   It should have: contains(), +, ++, map, flatMap, filter, and foreach

  case class MySet[T](f: T => Boolean) extends (T => Boolean) with Iterable[T] {
    override def apply(e: T): Boolean = f(e)

    override def iterator: Iterator[T] = ???
  }

  def contains[T](s: MySet[T], e: T): Boolean = s(e)

  def singletonSet[T](e: T): MySet[T] = MySet((x: T) => x == e)

  // Set that is all elements in s1 or  s2
  def union[T](s1: MySet[T], s2: MySet[T]): MySet[T] = MySet(x => s1(x) || s2(x))

  // Set that is all elements in both s1 and s2
  def intersection[T](s1: MySet[T], s2: MySet[T]): MySet[T] = MySet(x => s1(x) && s2(x))

  // Set that is all elements in s1 not in s2
  def diff[T](s1: MySet[T], s2: MySet[T]): MySet[T] = MySet((x:T) => s1(x) && !s2(x))

  // Set of elements from s where some predicate is true
  def filter[T](s: MySet[T], p: T => Boolean): MySet[T] = MySet(x => s(x) && p(x))

  // Returns whether all bounded integers within `s` satisfy `p`.
  def forall[T <: Iterable[T]](s: MySet[T], p: T => Boolean): Boolean = {
   false
  }





}