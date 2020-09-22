package old.lectures.exercises.part02oop

object BasicList {

  abstract class MyList {

    /*
        Implement a singly linked list of Integers
        methods:

        head = returns first element of the list
        tail = remainder of the list
        isEmpty = is this list empty
        add(int) = new list with this element add to the head
        toString => a string representation of the list
     */

    def head: Int
    def tail: MyList
    def isEmpty: Boolean
    def add(x:Int): MyList
    def ++: (x:Int): MyList

    protected def printElements: String
    override def toString: String = s"[ $printElements]"
  }

  object Empty extends MyList {
    def head: Int = throw new NoSuchElementException("head of empty list")
    def tail: MyList = throw new UnsupportedOperationException("tail of empty list")
    val isEmpty: Boolean = true
    def add(x: Int): MyList = new Cons(x)
    def ++: (x: Int): MyList = new Cons(x)
    def printElements: String = "Nil"
  }

  class Cons(h: Int, t:MyList = Empty) extends MyList {

    def head: Int = h
    def tail: MyList = t
    val isEmpty: Boolean = false
    def add(x: Int): MyList = new Cons(x, this)
    def ++: (x: Int): MyList = new Cons(x, this)


    def printElements: String = {

      @scala.annotation.tailrec
      def helper(n: MyList, acc: String): String = n match {
        case _: Empty.type => acc
        case ns:Cons => helper(ns.tail, s"$acc ${ns.head} ")
      }

      helper(n = this, acc ="")
    }
  }
}

object BasicListRunner extends App {
  import BasicList._
  val l = Empty.add(1).add(2).add(3)
  println(l.toString)

  val l2 = 1 ++: 2 ++: 3 ++: Empty  // Empty.++(3).++(2).++(1)

  // Empty ++ 1 ++ 2 ++ 3  //  Empty.++(1).++(2).++(3)
  println(l2.toString)

  val l3 = new Cons(1, Empty)
  println(l3.toString)

  val l4 = new Cons(1, new Cons(2, new Cons(3, Empty)))
  println(l4.toString)
}



