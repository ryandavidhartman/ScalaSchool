package lectures.polymorphism

import java.util.NoSuchElementException

/**
  * Type parameters are written in square brackets, e.g. [T].
  */

trait List[T] {
  def isEmpty: Boolean
  def head: T
  def tail: List[T]
}


class Cons[T](val head:T, val tail: List[T]) extends List[T] {
  def isEmpty: Boolean = false
}


class Nil[T]  extends List[T] {
  def isEmpty: Boolean = true

  def tail: Nothing = throw new NoSuchElementException("Nil.tail")

  def head: Nothing = throw new NoSuchElementException("Nil.head")
}


/**
  * Like classes, functions can have type parameters
  *
  * For instance, here is a function that creates a list consisting of a
  * single element
  */

object helpers {

  def singelton[T](elem: T): List[T] = new Cons[T](elem, new Nil[T])

  def nth[T](n: Int, list: List[T]): T = {
    if(n == 0 && !list.isEmpty) list.head
    else if(list.isEmpty || n < 0) throw new IndexOutOfBoundsException("oops!")
    else nth(n-1,list.tail)
  }
}

/**
  * Type parameters do not affect evaluations in Scala.
  *
  * We can assume that all type parameters and type arguments are removed
  * before evaluting the program.
  *
  * This is also called type erasure
  *
  * Languages that use type erasure include Java, Scala, Haskell, ML, OCaml
  *
  * Some other langues keep the type parameters around at run time, these include
  * C++, C#, F#
  */

/**
  * Polymorphism means that a function type comes "in many forms"
  *
  * In Programming it means that
  *   * the function can be applied to arguments of many types (generics), or
  *   * the type can have instances of many types (subclassing)
  *
  *   We have seen two principal forms of polymorphism:
  *
  *   Subtyping: instances of a subclass can be passed in place of the base class type
  *   generics: instances of a function or class are created by type parameterization.
  *
  */


