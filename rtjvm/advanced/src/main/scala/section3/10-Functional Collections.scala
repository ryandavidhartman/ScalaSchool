package section3

import scala.annotation.tailrec

/*

  EXERCISE #1
    - implement a functional set
  EXERCISE #2
    - removing an element
    - intersection with another set
    - difference with another set
  EXERCISE #3
    - a unary_! = NEGATION of a set
 */

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

    def unary_!(): MySet[T]
  }


  // IMPL ONE -> A totally functional set!
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

    def unary_!(): MySet[T] = FunSet[T]( (x: T) => !this(x))
  }



  // IMPL TWO -> A "SET" which is implemented at a Linked List

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

    // Again this is a tricky one!  So I'll cheat
    def unary_!(): MySet[T] = FunSet( (x:T) => true)

    override def toString(): String = ""
  }

  case class ConsSet[T](h: T, tail: MySet[T]) extends MySet[T] {
    def apply(e: T): Boolean = if(e == h) true else tail.contains(e)

    def +(e: T): MySet[T] = ConsSet(e, this)
    def ++(s: MySet[T]): MySet[T] = tail ++ ConsSet(h,s)

    def map[U](f: T => U): MySet[U] = ConsSet(f(h), tail.map(f))
    def flatMap[U](f: T => MySet[U]): MySet[U] = f(h) ++ tail.flatMap(f)
    def filter(p: T => Boolean): MySet[T] =
      if(p(h))
        tail.filter(p) + h
      else
        tail.filter(p)

    def foreach(f: T => Unit): Unit = {
      f(h)
      tail.foreach(f)
    }

    def -(e: T): MySet[T] = if(h == e) tail.-(e) else tail.-(e).+(h)
    def --(s: MySet[T]): MySet[T] = if(s.contains(h)) tail.--(s) else tail.--(s).+(h)
    def &(s: MySet[T]): MySet[T] = if(s.contains(h)) tail.&(s).+(h) else tail.&(s)

    // Again this is super hard, so we cheat
    def unary_!(): MySet[T] = FunSet( (x:T) => !this(x))

    override def toString(): String = {

      @tailrec
      def helper(acc: String, remainder: MySet[T]): String = remainder match {
        case _: EmptySet[T] => acc
        case s: ConsSet[T] => {
          if (acc.isEmpty)
            helper(s.h.toString, s.tail)
          else
            helper(s"${s.h}, $acc", s.tail)
        }
      }

      "[" + helper("", this) + "]"
    }
  }


  object MySet {
    def apply[T](values: T*): MySet[T] = {

      @tailrec
      def buildSet(valSeq: Seq[T], acc: MySet[T]): MySet[T] =
        if (valSeq.isEmpty)
          acc
        else
          buildSet(valSeq.tail, acc + valSeq.head)

      buildSet(values.zipWithIndex.toMap.keys.toSeq, new EmptySet[T])
    }
  }

  // Try it out

  val set1 = MySet(1,1,2,4,2323,2,3,4)
  println(s"set1: $set1")

  val set2 = set1.map(_ * 10)
  println(s"set2: $set2")

  val set3 = set1.flatMap(e => MySet(e, e*2))
  println(s"set3: $set3")

  val myFuncSet1 = FunSet((x:Int) => x%2 == 0)

  println(s"Does myFuncSet1 contain 102?  ${myFuncSet1(102)}")
  println(s"Does !myFuncSet1 contain 102?  ${!myFuncSet1(102)}")

  val myFuncSet2 = myFuncSet1.filter(_ != 4)
  println(s"Does myFuncSet2 contain 2?  ${myFuncSet2(2)}")
  println(s"Does myFuncSet2 contain 4?  ${myFuncSet2(4)}")

  val myFuncSet3 = myFuncSet2 + 4
  println(s"Does myFuncSet3 contain 4?  ${myFuncSet3(4)}")

}