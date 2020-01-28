package exercises.part1oop

abstract  class MyList {

  /*
      Implement a singly linked list of Integers
      methods:

      head = returns first element of the list
      tail = remainder of the list
      isEmpty = is this list empty
      add(int) = new list with this element add to the head
      toString => a string representation of the list
   */

  def head(): Int
  def tail(): MyList
  def isEmpty: Boolean
  def add(x:Int): MyList
}

object MyEmptyList extends MyList {
  def head(): Int = throw new NoSuchElementException("head of empty list")
  def tail(): MyList = throw new UnsupportedOperationException("tail of empty list")
  val isEmpty: Boolean = true
  def add(x: Int): MyList = new MyNonEmptyList(x)
  override def toString: String = ""
}

class MyNonEmptyList(data: Int, nextElement:MyList = MyEmptyList) extends MyList {

  def head(): Int = data
  def tail(): MyList = nextElement
  val isEmpty: Boolean = false
  def add(x: Int): MyList = new MyNonEmptyList(x, this)

  override def toString: String = {

    helper:String

  }
}


