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

  trait MySet[T] extends (T => Boolean) {
    def apply(x: T): Boolean
    def contains(e: T): Boolean = this(e)

    def +(e: T): MySet[T]
    def ++(s: MySet[T]): MySet[T]

    def map[U](f: T => U): MySet[U]
    def flatMap[U](f: T => MySet[U]): MySet[U]
    def filter(p: T => Boolean): MySet[T]

    def foreach(f: T => Unit): Unit

    def -(e: T): MySet[T]
    def --(s: MySet[T]): MySet[T] // difference
    def &(s: MySet[T]): MySet[T]  // intersection
  }

  case class FunSet[T](f: T => Boolean) extends MySet[T] {
    def apply(x: T): Boolean = f(x)

    def +(e: T): MySet[T] = FunSet(x => this(x) || x == e)
    def ++(s: MySet[T]): MySet[T] = FunSet(x => this(x) || s(x))

    def map[U](f: T => U): MySet[U] = throw new NotImplementedError("map is hard for this type of set")
    def flatMap[U](f: T => MySet[U]): MySet[U] =  throw new NotImplementedError("flatMap is hard for this type of set")
    def foreach(f: T => Unit): Unit = throw new NotImplementedError("foreach might not even make sense for it")

    def filter(p: T => Boolean): MySet[T] = FunSet(x => this(x) && p(x))

    def -(e: T): MySet[T] = FunSet(x => this(x) || x != e)
    def --(s: MySet[T]): MySet[T] = FunSet(x => this(x) || !s(x))
    def &(s: MySet[T]): MySet[T] = FunSet(x => this(x) && s(x))
  }

  // Ok for technical reasons it is very difficult to simply make Set == T => Boolean  AND implement
  // map and flatMap.
  // You either need to be able to iterate over all of T.  (which is computationally impossible to do this
  // completely)  OR your function f from T to U in the map must be invertible (i.e. a bijection)
  //
  // To avoid this complications we'll simply fall-back to a cons list implementation.

  case class EmptySet[T]() extends MySet[T] {
    def apply(e: T): Boolean = false

    def +(e: T): MySet[T] = ConsSet(e, this)
    def ++(s: MySet[T]): MySet[T] = s

    def map[U](f: T => U): MySet[U] = EmptySet[U]()
    def flatMap[U](f: T => MySet[U]): MySet[U] = EmptySet[U]
    def filter(p: T => Boolean): MySet[T] = this

    def foreach(f: T => Unit): Unit = ()

    def -(e: T): MySet[T] = this
    def --(s: MySet[T]): MySet[T] = this
    def &(s: MySet[T]): MySet[T] = this
  }

  case class ConsSet[T](h: T, tail: MySet[T]) extends MySet[T] {
    def apply(e: T): Boolean = if(e == h) true else tail.contains(e)

    def +(e: T): MySet[T] = ConsSet(e, this)
    def ++(s: MySet[T]): MySet[T] = tail ++ ConsSet(h,s)

    def map[U](f: T => U): MySet[U] = ConsSet(f(h), tail.map(f))
    def flatMap[U](f: T => MySet[U]): MySet[U] = f(h) ++ tail.flatMap(f)
    def filter(p: T => Boolean): MySet[T] =
      if(p(h))
        h + tail.filter(p)
      else
        tail.filter(p)

    def foreach(f: T => Unit): Unit = {
      f(h)
      tail.foreach(f)
    }

    def -(e: T): MySet[T] = if(h == e) tail.-(e) else h + tail.-(e)
    def --(s: MySet[T]): MySet[T] = if(s.contains(h)) tail.--(s) else h + tail.--(s)
    def &(s: MySet[T]): MySet[T] = if(s.contains(h)) h + tail.&(s) else tail.&(s)
  }



}