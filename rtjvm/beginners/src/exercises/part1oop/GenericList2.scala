package exercises.part1oop

object GenericList2 {

  /*
  1. Generic trait MyPredicate[-T] with a method def test(t:T): Boolean
  2. Generic trait MyTransformer[-T, U] with a method def tranform(t:T): U
  3. Add to MyList
      - map(transformer) => MyList
      - filter(predicate) => MyList
      - flatMap(transformer from T to MyList[U]) => MyList[U]

      class EvenPredicate extends MyPredicate[Int]
      class StringToIntTransformer extends MyTransformer[String, Int]

      [1,2,3].map(n *2) = [2,4,6]
      [1,2,3,4].filter(n%2) = [2,4]
      [1,2,3].flatMap(n => [n, n+1]) => [1,2,2,3,3,4]
   */

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
    def ++: [U >: T](x:U): MyList[U]
    def +: [U >: T](xs:MyList[U]): MyList[U]

    protected def printElements: String
    override def toString: String = s"[ $printElements]"

    def map[U](t: MyTransformer[T,U]): MyList[U]
    def filter(p: MyPredicate[T]): MyList[T]
    def flatMap[U](t: MyTransformer[T,MyList[U]]): MyList[U]
  }

  object Empty extends MyList[Nothing] {
    def head: Nothing = throw new NoSuchElementException("head of empty list")
    def tail: Nothing = throw new UnsupportedOperationException("tail of empty list")
    val isEmpty: Boolean = true
    def add[T](x: T): MyList[T] = new Cons(x)
    def ++: [T](x: T): MyList[T] = new Cons(x)
    def +: [T](xs: MyList[T]): MyList[T] = xs

    def printElements: String = "Nil"

    def map[U](t: MyTransformer[Nothing,U]): MyList[U]  = Empty
    def filter(p: MyPredicate[Nothing]): MyList[Nothing] = Empty
    def flatMap[U](t: MyTransformer[Nothing,MyList[U]]): MyList[U] = Empty

  }

  class Cons[+T](h: T, t:MyList[T] = Empty) extends MyList[T] {

    def head: T = h
    def tail: MyList[T] = t
    val isEmpty: Boolean = false
    def add[U >: T](x: U): MyList[U] = new Cons(x, this)
    def ++: [U >:T](x: U): MyList[U] = new Cons(x, this)
    def +: [U >: T](xs: MyList[U]): MyList[U] = xs match {
      case _:Empty.type => this
      case ns:Cons[U] => ns.head ++: (ns.tail +: this)
    }

    def printElements: String = {

      @scala.annotation.tailrec
      def helper(n: MyList[T], acc: String): String = n match {
        case _: Empty.type => acc
        case ns:Cons[T] => helper(ns.tail, s"$acc ${ns.head} ")
      }

      helper(n = this, acc ="")
    }

    override def map[U](t: MyTransformer[T, U]): MyList[U] =
      t.transform(this.head) ++: tail.map(t)

    override def filter(p: MyPredicate[T]): MyList[T] =
      if(p.test(h))
        h ++: tail.filter(p)
      else
        tail.filter(p)


    override def flatMap[U](t: MyTransformer[T, MyList[U]]): MyList[U] = {
      val result:MyList[U] = t.transform(head)
      val rest:MyList[U] = tail.flatMap(t)
      result +: rest
    }

  }
}

object GenericListRunner2 extends App {
  import GenericList2._
  val l = Empty.add(1).add(2).add(3)
  println(l.toString)

  val l2 = 1 ++: 2 ++: 3 ++: Empty
  println("l2 is: " + l2.toString)

  val l3 = new Cons(4, new Cons(5, Empty))
  println("l3 is: " + l3.toString)

  val l4 = l2 +: l3
  println(s"l2 + l3 is:  $l4")

  val l5 = l4.map(_ * 2)
  println(l5)

  val l6 = l4.flatMap(x => x ++: (x+1) ++: Empty)
  println(l6)

}

