package section4

/*
Same as GenericList4 but now
we replace all FunctionX calls with lambdas in the Generic List
 */

object GenericList5 {

  abstract class MyList5[+T] {

    def head: T
    def tail: MyList5[T]
    def isEmpty: Boolean
    def add[U >: T](x:U): MyList5[U]
    def +: [U >: T](x:U): MyList5[U]
    def ++ [U >: T](xs:MyList5[U]): MyList5[U]

    protected def printElements: String
    override def toString: String = s"[ $printElements]"

    def map[U](t: T => U): MyList5[U]
    def filter(p: T => Boolean): MyList5[T]
    def flatMap[U](t: T => MyList5[U]): MyList5[U]

    override def clone(): AnyRef = this match {
      case Empty5 => Empty5
      case l: MyList5[T] => Cons5(l.head, l.tail)
    }
  }

  case object Empty5 extends MyList5[Nothing] {
    def head: Nothing = throw new NoSuchElementException("head of empty list")
    def tail: Nothing = throw new UnsupportedOperationException("tail of empty list")
    val isEmpty: Boolean = true
    def add[T](x: T): MyList5[T] = Cons5(x)
    def +: [T](x: T): MyList5[T] = Cons5(x)
    def ++ [T](xs: MyList5[T]): MyList5[T] = xs

    def printElements: String = "Nil"

    def map[U](t: Nothing => U): MyList5[U]  = Empty5
    def filter(p: Nothing => Boolean): MyList5[Nothing] = Empty5
    def flatMap[U](t: Nothing => MyList5[U]): MyList5[U] = Empty5
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

    override def map[U](t: T => U): MyList5[U] =
      t(this.head) +: tail.map(t)

    override def filter(p: T => Boolean): MyList5[T] =
      if(p(h))
        h +: tail.filter(p)
      else
        tail.filter(p)


    override def flatMap[U](t: T => MyList5[U]): MyList5[U] = {
      val result:MyList5[U] = t(head)
      val rest:MyList5[U] = tail.flatMap(t)
      result ++ rest
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

}
