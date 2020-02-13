package exercises.part3fp

import exercises.part1oop.Generics.MyList
import exercises.part3fp.GenericList5.Empty5


// Expand MyList to include a foreach method T => Unit
// [1,2,3].foreach(x => println(x)

// Expand MyList to include a sort function ((A, A) => Int) => MyList
// [1,2,3].sort((x,y) => y - x) => [3,2,1]

// Expand MyList to include a zipWith (list, (A, A) => B => MyList[B]
// [1,2,3].zipWith[4,5,6], x*y) => [1*4, 2*5, 3*6]

// Expand MyList to include a fold. fold(start)(function) => a value
// [1,2,3].fold(0)(x+y) = 0+1 => 1 (1+2) => 3 (3+3) = 6



object GenericList5 {

  type MyPredicate[-T] = T => Boolean
  type MyTransformer[-T, U] = T => U
  type MySideEffect[-T] = T => Unit
  type MyComparer[-T] = (T, T) => Int
  type MyZipper[-T, U, V] = (T, U) => V
  type MyFolder[-T, U] = (T, U) => U

  abstract class MyList5[+T] {

    def head: T
    def tail: MyList5[T]
    def isEmpty: Boolean
    def add[U >: T](x:U): MyList5[U]
    def +: [U >: T](x:U): MyList5[U]
    def ++ [U >: T](xs:MyList5[U]): MyList5[U]

    protected def printElements: String
    override def toString: String = s"[ $printElements]"

    def map[U](t: MyTransformer[T,U]): MyList5[U]
    def filter(p: MyPredicate[T]): MyList5[T]
    def flatMap[U](t: MyTransformer[T,MyList5[U]]): MyList5[U]

    override def clone(): AnyRef = this match {
      case Empty5 => Empty5
      case l:MyList5[T] => Cons5(l.head, l.tail)
    }

    //new stuff!
    def foreach(sideEffect: MySideEffect[T]): Unit
    def sort(comparer: MyComparer[T]): MyList5[T]
    def getLargest(comparer: MyComparer[T]): (T, MyList5[T])
    def zipWith[U,V](list: MyList5[U], zipper: MyZipper[T, U, V]): MyList5[V]
    def fold[U](zero:U)(folder: MyFolder[T, U]): U
  }

  case object Empty5 extends MyList5[Nothing] {
    def head: Nothing = throw new NoSuchElementException("head of empty list")
    def tail: Nothing = throw new UnsupportedOperationException("tail of empty list")
    val isEmpty: Boolean = true
    def add[T](x: T): MyList5[T] = Cons5(x)
    def +: [T](x: T): MyList5[T] = Cons5(x)
    def ++ [T](xs: MyList5[T]): MyList5[T] = xs

    def printElements: String = "Nil"

    def map[U](t: MyTransformer[Nothing,U]): MyList5[U]  = Empty5
    def filter(p: MyPredicate[Nothing]): MyList5[Nothing] = Empty5
    def flatMap[U](t: MyTransformer[Nothing,MyList5[U]]): MyList5[U] = Empty5

    // new stuff
    def foreach(sideEffect: MySideEffect[Nothing]): Unit = Unit
    def sort(comparer: MyComparer[Nothing]): MyList5[Nothing] = Empty5
    def getLargest(comparer: MyComparer[Nothing]): (Nothing, MyList5[Nothing]) =
      throw new UnsupportedOperationException("getLargest of empty list")
    def zipWith[U, V](list: MyList5[U], zipper: MyZipper[Nothing, U, V]): MyList5[V] = Empty5
    def fold[U](zero: U)(folder: MyFolder[Nothing, U]): U = zero
  }

  case class Cons5[+T](h: T, t:MyList5[T] = Empty5) extends MyList5[T] {

    def head: T = h
    def tail: MyList5[T] = t
    val isEmpty: Boolean = false
    def add[U >: T](x: U): MyList5[U] = Cons5(x, this)
    def +: [U >:T](x: U): MyList5[U] = Cons5(x, this)
    def ++ [U >: T](xs: MyList5[U]): MyList5[U] = Cons5(head, tail ++ xs)


    def printElements: String = {

      @scala.annotation.tailrec
      def helper(n: MyList5[T], acc: String): String = n match {
        case _: Empty5.type => acc
        case ns:Cons5[T] => helper(ns.tail, s"$acc ${ns.head} ")
      }

      helper(n = this, acc ="")
    }

    override def map[U](t: MyTransformer[T, U]): MyList5[U] =
      t(this.head) +: tail.map(t)

    override def filter(p: MyPredicate[T]): MyList5[T] =
      if(p(h))
        h +: tail.filter(p)
      else
        tail.filter(p)


    override def flatMap[U](t: MyTransformer[T, MyList5[U]]): MyList5[U] = {
      val result:MyList5[U] = t(head)
      val rest:MyList5[U] = tail.flatMap(t)
      result ++ rest
    }

    // new stuff

     def foreach(sideEffect: MySideEffect[T]): Unit = {
       sideEffect(head)
       tail.foreach(sideEffect)
     }

     def sort(comparer: MyComparer[T]): MyList5[T] = {

       def sortHelper(list: MyList5[T]): MyList5[T] = list match {
         case Empty5 => Empty5
         case l: Cons5[T] =>
           val (biggest, remainder) = l.getLargest(comparer)
           val remainderSorted = sortHelper(remainder)
           Cons5(biggest, remainderSorted)
       }

       sortHelper(this)
     }

     def getLargest(comparer: MyComparer[T]): (T, MyList5[T]) = {
       def getLargestHelper(list: MyList5[T]): (T, MyList5[T]) = list match {
         case Cons5(h, Empty5) => (h, Empty5)
         case Cons5(h, tail) =>
         val (largestInTail, leftInTail) = getLargestHelper(tail)

         if(comparer(h, largestInTail) >= 0)
           (largestInTail, Cons5(h, leftInTail))
         else
           (head, Cons5(largestInTail, leftInTail))
       }
       getLargestHelper(this)
     }

    def zipWith[U, V](list: MyList5[U], zipper: MyZipper[T, U, V]): MyList5[V] = {

      //We probably should add some checks for lists of different lengths!
      val newHead = zipper(head, list.head)
      Cons5(newHead, tail.zipWith(list.tail, zipper))
    }

    override def fold[U](zero: U)(folder: MyFolder[T, U]): U = {

      @scala.annotation.tailrec
      def fold_helper(acc:U, list:MyList5[T]): U = list match {
        case Empty5 => acc
        case Cons5(h,t) => fold_helper(folder(h, acc), t)
      }

      fold_helper(zero, this)

    }
  }
}

object GenericListRunner5 extends App {
  import GenericList5._
  val l = Empty5.add(1).add(2).add(3)
  println(l.toString)

  val l2 = 1 +: 2 +: 3 +: Empty5
  println("l2 is: " + l2.toString)

  val l3 = Cons5(4, Cons5(5, Empty5))
  println("l3 is: " + l3)

  val l4 = l2 ++ l3
  println(s"l2 + l3 is:  $l4")

  val l5 = l4.map(_ * 2)
  println(l5)

  val l6 = l4.flatMap(x => x +: (x+1) +: Empty5)
  println(l6)

  val l7 = l6.clone()
  println(l7 == l6)

  // new tests for HofsCurries Homework

  // these 3 ways of calling foreach is the same!
  l2.foreach(i => println(i))
  l2.foreach(println(_))
  l2.foreach(println)

  // tests for sort
  val intComparer:MyComparer[Int] = (l:Int, r:Int) => r - l

  println("l2 -> get largest: " + l2.getLargest(intComparer))
  println("l4 -> get largest: " + l4.getLargest(intComparer))

  val tiny = Cons5(1, Empty5)
  println("tiny -> get largest: " + tiny.getLargest(intComparer))
  val tiny2 = Cons5(-3, tiny)
  println("tiny2 -> get largest: " + tiny2.getLargest(intComparer))

  println("Here is tiny sorted: " + tiny.sort(intComparer))
  println("Here is tiny2 sorted: " + tiny2.sort(intComparer))
  println("Here is l2 sorted: " + l2.sort(intComparer))
  println("Here is l4 sorted: " + l4.sort(intComparer))


  // tests for Zip
  val l8 = 4 +: 5 +: 6 +: Empty5
  val zipperFunction = (l:Int, r:Int) => l+r
  val l9 = l2.zipWith(l8, zipperFunction)
  println("l2.zipWith(l8, zipperFunction): " + l9)

  // tests for fold
  val summer = (l: Int, r:Int) => l+r

  val sum = l2.fold(0)(summer)
  println("The sum of the elements in l2 is: " + sum)



}

