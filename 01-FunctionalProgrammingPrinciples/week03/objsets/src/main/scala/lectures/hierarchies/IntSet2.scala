package lectures.hierarchies

abstract class IntSet2 {
  def incl(x: Int): IntSet2
  def contains(x:Int): Boolean
  def union(other: IntSet2): IntSet2
}

/**
  * In the original IntSet example, one could argue that there is really only a
  * single empty IntSet.
  *
  * So it seems overkill to have the user crate many instances of it.
  * We can express this case better with an object definition.
  *
  * This defines a singelton object named Empty.
  * No other Empty instances can be (or need to be) created.
  * Singleton objects are values so Empty evaluates to itself.
  */

object Empty2 extends IntSet2 {
  def incl(x: Int): IntSet2 = new NonEmpty2(x, Empty2, Empty2)

  def contains(x: Int): Boolean = false

  override def union(other: IntSet2): IntSet2 = other

  override def toString = "."

}


case class NonEmpty2(elem: Int, left: IntSet2, right: IntSet2) extends IntSet2 {
  def incl(x: Int): IntSet2 =
    if(x < elem) new NonEmpty2(elem, left incl x, right)
    else if (x > elem) new NonEmpty2(elem, left, right incl x)
    else this

  def contains(x: Int): Boolean =
    if(x < elem) left contains x
    else if(x > elem) right contains x
    else true

  override def union(other: IntSet2): IntSet2 = ((left union right) union other) incl elem
  override def toString = s"{ ${left} $elem ${right} }"
}

/**
  * Dynamic Binding
  *
  * Object-oriented languages (including Scala) implement dynamic method dispatch.
  *
  * This means that the code invoked by a method call depends on the runtime type of the object
  * that contains the method
  *
  * Example:
  *
  * Empty Contains 1 -> [1/x][Empty/this] false = false
  *
  * Example:
  *
  * (new NonEmpty(7, Empty, Empty)) contains 7 ->
  * [7/elem][7/x][new NonEmpty(7,Empty,Empty)/this]
  *    if(x < elem) left contains x
  *    else if(x > elem) right contains x
  *    else true  ->
  *
  *    if(7 < 7) new NonEmpty(7,Empty,Empty).left contains 7
  *    else if(7 > 7) new NonEmpty(7,Empty,Empty.right contains 7
  *    else true ->
  *    true
  *
  *
  */

