package exercises.part1oop

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


  abstract class MyList[+T] {

    def head: T
    def tail: MyList[T]
    def isEmpty: Boolean
    def add[U >: T](x:U): MyList[U]
    def +: [U >: T](x:U): MyList[U]
    def ++ [U >: T](xs:MyList[U]): MyList[U]

    protected def printElements: String
    override def toString: String = s"[ $printElements]"

    def map[U](t: MyTransformer[T,U]): MyList[U]
    def filter(p: MyPredicate[T]): MyList[T]
    def flatMap[U](t: MyTransformer[T,MyList[U]]): MyList[U]
  }

  case object Empty extends MyList[Nothing] {
    def head: Nothing = throw new NoSuchElementException("head of empty list")
    def tail: Nothing = throw new UnsupportedOperationException("tail of empty list")
    val isEmpty: Boolean = true
    def add[T](x: T): MyList[T] = new Cons(x)
    def +: [T](x: T): MyList[T] = new Cons(x)
    def ++ [T](xs: MyList[T]): MyList[T] = xs

    def printElements: String = "Nil"

    def map[U](t: MyTransformer[Nothing,U]): MyList[U]  = Empty
    def filter(p: MyPredicate[Nothing]): MyList[Nothing] = Empty
    def flatMap[U](t: MyTransformer[Nothing,MyList[U]]): MyList[U] = Empty

  }

  case class Cons[+T](h: T, t:MyList[T] = Empty) extends MyList[T] {

    def head: T = h
    def tail: MyList[T] = t
    val isEmpty: Boolean = false
    def add[U >: T](x: U): MyList[U] = new Cons(x, this)
    def +: [U >:T](x: U): MyList[U] = new Cons(x, this)
    def ++ [U >: T](xs: MyList[U]): MyList[U] = new Cons(head, tail ++ xs)


    def printElements: String = {

      @scala.annotation.tailrec
      def helper(n: MyList[T], acc: String): String = n match {
        case _: Empty.type => acc
        case ns:Cons[T] => helper(ns.tail, s"$acc ${ns.head} ")
      }

      helper(n = this, acc ="")
    }

    override def map[U](t: MyTransformer[T, U]): MyList[U] =
      t.transform(this.head) +: tail.map(t)

    override def filter(p: MyPredicate[T]): MyList[T] =
      if(p.test(h))
        h +: tail.filter(p)
      else
        tail.filter(p)


    override def flatMap[U](t: MyTransformer[T, MyList[U]]): MyList[U] = {
      val result:MyList[U] = t.transform(head)
      val rest:MyList[U] = tail.flatMap(t)
      result ++ rest
    }

  }
}

object GenericListRunner3 extends App {
  import GenericList3._
  val l = Empty.add(1).add(2).add(3)
  println(l.toString)

  val l2 = 1 +: 2 +: 3 +: Empty
  println("l2 is: " + l2.toString)

  val l3 = Cons(4, Cons(5, Empty))
  println("l3 is: " + l3)

  val l4 = l2 ++ l3
  println(s"l2 + l3 is:  $l4")

  val l5 = l4.map(_ * 2)
  println(l5)

  val l6 = l4.flatMap(x => x +: (x+1) +: Empty)
  println(l6)

  val l7 = l6.clone()
  println(l7 == l6)

}

