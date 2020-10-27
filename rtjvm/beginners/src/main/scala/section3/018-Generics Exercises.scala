package section3

object BasicList2 {

  /*
    Implement a singly linked list of Integers
    methods:

    head = returns first element of the list
    tail = remainder of the list
    isEmpty = is this list empty
    add(int) => new list with this element add to the head
    toString => a string representation of the list
 */

  abstract class MyList[+T] {
    def head: T
    def tail: MyList[T]
    def isEmpty: Boolean
    def add[U >: T](x: U): MyList[U]
    def ++: [U >: T](x: U): MyList[U]

    protected def printElements: String
    override def toString: String = s"[$printElements]"
  }

  object Empty extends MyList[Nothing] {
    def head = throw new NoSuchElementException("head of empty list")
    def tail = throw new UnsupportedOperationException("tail of empty list")
    val isEmpty: Boolean = true
    def add[T](x: T): MyList[T] = new Cons(x)
    def ++: [T](x: T): MyList[T] = new Cons(x)
    def printElements: String = "Nil"
  }

  class Cons[+T](h: T, t: MyList[T] = Empty) extends MyList[T] {

    def head: T = h
    def tail: MyList[T] = t
    val isEmpty: Boolean = false
    def add[U >: T](x: U): MyList[U] = new Cons(x, this)
    def ++: [U >: T](x: U): MyList[U] = new Cons(x, this)

    def printElements: String = {
      @scala.annotation.tailrec
      def helper(n: MyList[T], acc: String): String = if(n.isEmpty) {
        acc
      } else {
        helper(n.tail, s"$acc ${n.head} ")
      }
      helper(n = this, acc ="")
    }
  }

}


object BasicListRunner2 extends App {
  import BasicList2._
  val l = Empty.add(1).add(2).add(3)
  println(l.toString)

  val l2 = 1 ++: 2 ++: 3 ++: Empty  // Empty.++(3).++(2).++(1)

  //Empty ++ 1 ++ 2 ++ 3  //  Empty.++(1).++(2).++(3)
  println(l2.toString)

  val l3 = new Cons(1, Empty)
  println(l3.toString)

  val l4 = new Cons(1, new Cons(2, new Cons(3, Empty)))
  println(l4.toString)

  val stringList = "a" ++: "b" ++: "c" ++: Empty
  println(stringList)
}



