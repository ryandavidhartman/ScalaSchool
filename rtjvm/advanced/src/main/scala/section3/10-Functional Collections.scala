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
  // It should have: contains(), +, ++, map, flatMap, filter, and foreach
  // Ok for technical reasons it is very difficult to simply make Set == T => Boolean  AND implement
  // map and flatMap.
  // You either need to be able to iterate over all of T.  (which is computationally impossible to do this
  // completely)  OR your function f from T to U in the map must be invertible (i.e. a bijection)
  //
  // To avoid this complications we'll simply fall-back to a cons list implementation.

  trait MySet[T] extends (T => Boolean) {
    def contains(e: T): Boolean = this(e)

    def +(e: T): MySet[T]
    def ++(s: MySet[T]): MySet[T]

    def map[U](f: T => U): MySet[U]
    def flatMap[U](f: T => MySet[U]): MySet[U]
    def filter(p : T => Boolean): MySet[T]
  }

  case class EmptySet[T]() extends MySet[T] {
    def apply(e: T): Boolean = false
    def +(e: T): MySet[T] = ConsSet(e, this)
    def ++(s: MySet[T]): MySet[T] = s
    def map[U](f: T => U): MySet[U] = EmptySet[U]()
    def flatMap[U](f: T => MySet[U]): MySet[U] = EmptySet[U]
    def filter(p: T => Boolean): MySet[T] = this
  }

  case class ConsSet[T](h: T, tail: MySet[T]) extends MySet[T] {
    def apply(e: T): Boolean = if(e == h) true else tail.contains(e)
    override def +(e: T): MySet[T] = ConsSet(e, this)
    override def ++(s: MySet[T]): MySet[T] = ???
    override def map[U](f: T => U): MySet[U] = ???
    override def flatMap[U](f: T => MySet[U]): MySet[U] = ???
    override def filter(p: T => Boolean): MySet[T] = ???
  }



}