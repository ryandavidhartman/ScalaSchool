package exercises.part1oop

object GenericList {

  abstract class MyList[+T] {
    def head: T
    def tail: MyList[T]
    def isEmpty: Boolean
    def add[U >: T](x:U): MyList[U]
    def ++: [U >: T](x:U): MyList[U]

    protected def printElements: String
    override def toString: String = s"[ $printElements]"
  }

  object Empty extends MyList[Nothing] {
    def head: Nothing = throw new NoSuchElementException("head of empty list")
    def tail: Nothing = throw new UnsupportedOperationException("tail of empty list")
    val isEmpty: Boolean = true
    def add[T](x: T): MyList[T] = new Cons(x)
    def ++: [T](x: T): MyList[T] = new Cons(x)
    def printElements: String = "Nil"

  }

  class Cons[+T](h: T, t:MyList[T] = Empty) extends MyList[T] {

    def head: T = h
    def tail: MyList[T] = t
    val isEmpty: Boolean = false
    def add[U >: T](x: U): MyList[U] = new Cons(x, this)
    def ++: [U >:T](x: U): MyList[U] = new Cons(x, this)


    def printElements: String = {

      @scala.annotation.tailrec
      def helper(n: MyList[T], acc: String): String = n match {
        case _: Empty.type => acc
        case ns:Cons[T] => helper(ns.tail, s"$acc ${ns.head} ")
      }

      helper(n = this, acc ="")
    }
  }
}

object GenericListRunner extends App {
  import GenericList._
  val l = Empty.add(1).add(2).add(3)
  println(l.toString)

  val l2 = 1 ++: 2 ++: 3 ++: Empty
  println(l2.toString)

  val l3 = new Cons(1, Empty)
  println(l3.toString)

  val l4 = new Cons("a", new Cons("b", new Cons("c", Empty)))
  println(l4.toString)
}
