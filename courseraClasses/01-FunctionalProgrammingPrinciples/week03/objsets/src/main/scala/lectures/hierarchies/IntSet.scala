package lectures.hierarchies

/**
  * Abstract classes can contain members which are missing an implementation
  * Consequently, no instances of an abstract class can be created with the
  * operator new
  */

abstract class IntSet {
  def incl(x: Int): IntSet
  def contains(x:Int): Boolean
}

/**
  * Empty and NonEmpty both extend the class IntSet
  *
  * This implies that the types Empty and NonEmpty conform to the type IntSet
  * therefore an object of type Empty or NonEmpty can be used wherever an object
  * of type IntSet is required
  */

class Empty extends IntSet {
  def incl(x: Int): IntSet = new NonEmpty(x, new Empty, new Empty)

  def contains(x: Int): Boolean = false

  override def toString = "."
}

/**
  * The definitions of contains and incl in the classes Empty and
  * NonEmpty implement the abstract functions in the base trait IntSet
  */
case class NonEmpty(elem: Int, left: IntSet, right: IntSet) extends IntSet {
  def incl(x: Int): IntSet =
    if(x < elem) new NonEmpty(elem, left incl x, right)
    else if (x > elem) new NonEmpty(elem, left, right incl x)
    else this

  def contains(x: Int): Boolean =
    if(x < elem) left contains x
    else if(x > elem) right contains x
    else true

  override def toString = s"{ ${left} $elem ${right} }"
}

/**
  * IntSet is called the superclass of Empty and NonEmpty. Empty and NonEmpty are subclasses of IntSet
  *
  * In Scala, any user-defined class extends another class.
  *
  * If no superclass is given, the standard class Object in the Java
  * package java.lang is assumed
  *
  * The direct or indirect superclasses of a class C are called base classes
  * of C.
  * So, the base classes of NonEmpty are IntSet and object
 */
