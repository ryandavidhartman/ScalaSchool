package exercises.part02oop

/*
Same as GenericList2 just using case classes and case objects
 */

object GenericList3 {

  trait MyPredicate[-T] {
    def test(t:T): Boolean
  }

  trait MyTransformer[-T, U] {
    def transform(t:T): U
  }

  abstract class MyList3[+T] {

    def head: T
    def tail: MyList3[T]
    def isEmpty: Boolean
    def add[U >: T](x:U): MyList3[U]
    def +: [U >: T](x:U): MyList3[U]
    def ++ [U >: T](xs:MyList3[U]): MyList3[U]

    protected def printElements: String
    override def toString: String = s"[ $printElements]"

    def map[U](t: MyTransformer[T,U]): MyList3[U]
    def filter(p: MyPredicate[T]): MyList3[T]
    def flatMap[U](t: MyTransformer[T,MyList3[U]]): MyList3[U]

    override def clone(): AnyRef = this match {
      case Empty3 => Empty3
      case l:MyList3[T] => Cons3(l.head, l.tail)
    }
  }

  case object Empty3 extends MyList3[Nothing] {
    def head: Nothing = throw new NoSuchElementException("head of empty list")
    def tail: Nothing = throw new UnsupportedOperationException("tail of empty list")
    val isEmpty: Boolean = true
    def add[T](x: T): MyList3[T] = Cons3(x)
    def +: [T](x: T): MyList3[T] = Cons3(x)
    def ++ [T](xs: MyList3[T]): MyList3[T] = xs

    def printElements: String = "Nil"

    def map[U](t: MyTransformer[Nothing,U]): MyList3[U]  = Empty3
    def filter(p: MyPredicate[Nothing]): MyList3[Nothing] = Empty3
    def flatMap[U](t: MyTransformer[Nothing,MyList3[U]]): MyList3[U] = Empty3
  }

  case class Cons3[+T](h: T, t:MyList3[T] = Empty3) extends MyList3[T] {

    def head: T = h
    def tail: MyList3[T] = t
    val isEmpty: Boolean = false
    def add[U >: T](x: U): MyList3[U] = Cons3(x, this)
    def +: [U >:T](x: U): MyList3[U] = Cons3(x, this)
    def ++ [U >: T](xs: MyList3[U]): MyList3[U] = Cons3(head, tail ++ xs)


    def printElements: String = {

      @scala.annotation.tailrec
      def helper(n: MyList3[T], acc: String): String = n match {
        case _: Empty3.type => acc
        case ns:Cons3[T] => helper(ns.tail, s"$acc ${ns.head} ")
      }

      helper(n = this, acc ="")
    }

    override def map[U](t: MyTransformer[T, U]): MyList3[U] =
      t.transform(this.head) +: tail.map(t)

    override def filter(p: MyPredicate[T]): MyList3[T] =
      if(p.test(h))
        h +: tail.filter(p)
      else
        tail.filter(p)


    override def flatMap[U](t: MyTransformer[T, MyList3[U]]): MyList3[U] = {
      val result:MyList3[U] = t.transform(head)
      val rest:MyList3[U] = tail.flatMap(t)
      result ++ rest
    }

  }
}

object GenericListRunner3 extends App {
  import GenericList3._
  val l = Empty3.add(1).add(2).add(3)
  println(l.toString)

  val l2 = 1 +: 2 +: 3 +: Empty3
  println("l2 is: " + l2.toString)

  val l3 = Cons3(4, Cons3(5, Empty3))
  println("l3 is: " + l3)

  val l4 = l2 ++ l3
  println(s"l2 + l3 is:  $l4")

  val l5 = l4.map(_ * 2)
  println(l5)

  val l6 = l4.flatMap(x => x +: (x+1) +: Empty3)
  println(l6)

  val l7 = l6.clone()
  println(l7 == l6)

}

