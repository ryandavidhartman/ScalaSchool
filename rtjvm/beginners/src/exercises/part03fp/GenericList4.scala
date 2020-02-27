package exercises.part03fp


/*
Same as GenericList3 now we
define myPredict, and MyTransformer as function types
 */

object GenericList4 {

  type MyPredicate[-T] = T => Boolean

  type MyTransformer[-T, U] = T => U

  abstract class MyList4[+T] {

    def head: T
    def tail: MyList4[T]
    def isEmpty: Boolean
    def add[U >: T](x:U): MyList4[U]
    def +: [U >: T](x:U): MyList4[U]
    def ++ [U >: T](xs:MyList4[U]): MyList4[U]

    protected def printElements: String
    override def toString: String = s"[ $printElements]"

    def map[U](t: MyTransformer[T,U]): MyList4[U]
    def filter(p: MyPredicate[T]): MyList4[T]
    def flatMap[U](t: MyTransformer[T,MyList4[U]]): MyList4[U]

    override def clone(): AnyRef = this match {
      case Empty4 => Empty4
      case l:MyList4[T] => Cons4(l.head, l.tail)
    }
  }

  case object Empty4 extends MyList4[Nothing] {
    def head: Nothing = throw new NoSuchElementException("head of empty list")
    def tail: Nothing = throw new UnsupportedOperationException("tail of empty list")
    val isEmpty: Boolean = true
    def add[T](x: T): MyList4[T] = Cons4(x)
    def +: [T](x: T): MyList4[T] = Cons4(x)
    def ++ [T](xs: MyList4[T]): MyList4[T] = xs

    def printElements: String = "Nil"

    def map[U](t: MyTransformer[Nothing,U]): MyList4[U]  = Empty4
    def filter(p: MyPredicate[Nothing]): MyList4[Nothing] = Empty4
    def flatMap[U](t: MyTransformer[Nothing,MyList4[U]]): MyList4[U] = Empty4
  }

  case class Cons4[+T](h: T, t:MyList4[T] = Empty4) extends MyList4[T] {

    def head: T = h
    def tail: MyList4[T] = t
    val isEmpty: Boolean = false
    def add[U >: T](x: U): MyList4[U] = Cons4(x, this)
    def +: [U >:T](x: U): MyList4[U] = Cons4(x, this)
    def ++ [U >: T](xs: MyList4[U]): MyList4[U] = Cons4(head, tail ++ xs)


    def printElements: String = {

      @scala.annotation.tailrec
      def helper(n: MyList4[T], acc: String): String = n match {
        case _: Empty4.type => acc
        case ns:Cons4[T] => helper(ns.tail, s"$acc ${ns.head} ")
      }

      helper(n = this, acc ="")
    }

    override def map[U](t: MyTransformer[T, U]): MyList4[U] =
      t(this.head) +: tail.map(t)

    override def filter(p: MyPredicate[T]): MyList4[T] =
      if(p(h))
        h +: tail.filter(p)
      else
        tail.filter(p)


    override def flatMap[U](t: MyTransformer[T, MyList4[U]]): MyList4[U] = {
      val result:MyList4[U] = t(head)
      val rest:MyList4[U] = tail.flatMap(t)
      result ++ rest
    }

  }
}

object GenericListRunner4 extends App {
  import GenericList4._
  val l = Empty4.add(1).add(2).add(3)
  println(l.toString)

  val l2 = 1 +: 2 +: 3 +: Empty4
  println("l2 is: " + l2.toString)

  val l3 = Cons4(4, Cons4(5, Empty4))
  println("l3 is: " + l3)

  val l4 = l2 ++ l3
  println(s"l2 + l3 is:  $l4")

  val l5 = l4.map(_ * 2)
  println(l5)

  val l6 = l4.flatMap(x => x +: (x+1) +: Empty4)
  println(l6)

  val l7 = l6.clone()
  println(l7 == l6)

}

