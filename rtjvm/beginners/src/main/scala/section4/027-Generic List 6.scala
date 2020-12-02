package section4

import section4.GenericList6.MyList6

import scala.annotation.tailrec

/*
Same as GenericList5 but now we have added:

  // Expand MyList to include a foreach method T => Unit
  // [1,2,3].foreach(x => println(x)

  // Expand MyList to include a sort function ((A, A) => Int) => MyList
  // [1,2,3].sort((x,y) => y - x) => [3,2,1]
  // see exercises/part3fp/GenericList5.scala

  // Expand MyList to include a zipWith (list, (A, A) => B => MyList[B]
  // [1,2,3].zipWith[4,5,6], x*y) => [1*4, 2*5, 3*6]
  // see exercises/part3fp/GenericList5.scala

  // Expand MyList to include a fold. fold(start)(function) => a value
  // [1,2,3].fold(0)(x+y) = 0+1 => 1 (1+2) => 3 (3+3) = 6
  // see exercises/part3fp/GenericList5.scala

 */

object GenericList6 {

  abstract class MyList6[+T] {

    def head: T
    def tail: MyList6[T]
    def isEmpty: Boolean
    def add[U >: T](x:U): MyList6[U]
    def +: [U >: T](x:U): MyList6[U]
    def ++ [U >: T](xs:MyList6[U]): MyList6[U]

    protected def printElements: String
    override def toString: String = s"[ $printElements]"

    def map[U](t: T => U): MyList6[U]
    def filter(p: T => Boolean): MyList6[T]
    def flatMap[U](t: T => MyList6[U]): MyList6[U]

    override def clone(): MyList6[T] = this match {
      case Empty6 => Empty6
      case l: MyList6[T] => Cons6(l.head, l.tail)
    }

    val length: Int

    def foreach(f: T => Unit): Unit
    def sort(ordering: (T,T) => Int): MyList6[T]
  }

  case object Empty6 extends MyList6[Nothing] {
    def head: Nothing = throw new NoSuchElementException("head of empty list")
    def tail: Nothing = throw new UnsupportedOperationException("tail of empty list")
    val isEmpty: Boolean = true
    def add[T](x: T): MyList6[T] = Cons6(x)
    def +: [T](x: T): MyList6[T] = Cons6(x)
    def ++ [T](xs: MyList6[T]): MyList6[T] = xs

    def printElements: String = "Nil"

    def map[U](t: Nothing => U): MyList6[U]  = Empty6
    def filter(p: Nothing => Boolean): MyList6[Nothing] = Empty6
    def flatMap[U](t: Nothing => MyList6[U]): MyList6[U] = Empty6

    val length = 0

    def foreach(f: Nothing => Unit): Unit = ()
    def sort(ordering: (Nothing, Nothing) => Int): MyList6[Nothing] = Empty6
  }

  case class Cons6[+T](h: T, t:MyList6[T] = Empty6) extends MyList6[T] {

    def head: T = h
    def tail: MyList6[T] = t
    val isEmpty: Boolean = false
    def add[U >: T](x: U): MyList6[U] = Cons6(x, this)
    def +: [U >:T](x: U): MyList6[U] = Cons6(x, this)
    def ++ [U >: T](xs: MyList6[U]): MyList6[U] = Cons6(head, tail ++ xs)


    def printElements: String = {

      @scala.annotation.tailrec
      def helper(n: MyList6[T], acc: String): String = n match {
        case _: Empty6.type => acc
        case ns:Cons6[T] => helper(ns.tail, s"$acc ${ns.head} ")
      }

      helper(n = this, acc ="")
    }

    override def map[U](t: T => U): MyList6[U] =
      t(this.head) +: tail.map(t)

    override def filter(p: T => Boolean): MyList6[T] =
      if(p(h))
        h +: tail.filter(p)
      else
        tail.filter(p)

    override val length: Int = {
      @tailrec
      def helper[U](acc: Int, l: MyList6[U]): Int =
        if(l.isEmpty)
          acc
        else
          helper(acc+1, l.tail)

      helper(acc = 0, this)
    }


    override def flatMap[U](t: T => MyList6[U]): MyList6[U] = {
      val result:MyList6[U] = t(head)
      val rest:MyList6[U] = tail.flatMap(t)
      result ++ rest
    }

    def foreach(f: T => Unit): Unit = map(f)

    def sort(ordering: (T, T) => Int): MyList6[T] = {

      def merge(xs: MyList6[T], ys: MyList6[T]): MyList6[T] = (xs, ys) match {
        case (_, Empty6) => xs
        case (Empty6, _) => ys
        case (Cons6(x, xs1), Cons6(y, ys1)) =>
          if(ordering(x,y) < 0)
            x +: merge(xs1, ys)
          else
            y +: merge(xs, ys1)
      }

      def splitAt(n: Int): (MyList6[T], MyList6[T]) = {
        @tailrec
        def helper(left: MyList6[T], right: MyList6[T], i: Int): (MyList6[T], MyList6[T]) =
          if(i == 0)
            (left, right)
          else
            helper(right.head +: left, right.tail, i-1)

        helper(Empty6, this, n)
      }

      val halfWay = length/2
      if(halfWay == 0) {
        this
      } else {
        val (left, right) = splitAt(halfWay)
        merge(left.sort(ordering), right.sort(ordering))
      }
    }
  }
}


object GenericListRunner6 extends App {
  import GenericList6._
  val l = Empty6.add(1).add(2).add(3)
  println(l.toString)

  val l2 = 1 +: 2 +: 3 +: Empty6
  println("l2 is: " + l2.toString)

  val l3 = Cons6(4, Cons6(5, Empty6))
  println("l3 is: " + l3)

  val l4 = l2 ++ l3
  println(s"l2 + l3 is:  $l4")

  val l5 = l4.map(_ * 2)
  println(l5)

  val l6 = l4.flatMap(x => x +: (x+1) +: Empty6)
  println(l6)

  val l7 = l6.clone()
  println(l7 == l6)

  l7.foreach(i => println(s"Item: $i"))

  val unSorted = -1 +: 23 +: 12 +: -9 +: 100 +: 2 +: Empty6
  def standardOrder(x: Int, y: Int): Int =
    if(x > y) 1
    else if(x == y) 0
    else -1

  println(unSorted.length)
  println(unSorted.sort(standardOrder))

}
