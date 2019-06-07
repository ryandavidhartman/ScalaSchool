package example

import common._

import scala.annotation.tailrec

object Lists {
  /**
   * This method computes the sum of all elements in the list xs. There are
   * multiple techniques that can be used for implementing this method, and
   * you will learn during the class.
   *
   * For this example assignment you can use the following methods in class
   * `List`:
   *
   *  - `xs.isEmpty: Boolean` returns `true` if the list `xs` is empty
   *  - `xs.head: Int` returns the head element of the list `xs`. If the list
   *    is empty an exception is thrown
   *  - `xs.tail: List[Int]` returns the tail of the list `xs`, i.e. the the
   *    list `xs` without its `head` element
   *
   *  ''Hint:'' instead of writing a `for` or `while` loop, think of a recursive
   *  solution.
   *
   * @param xs A list of natural numbers
   * @return The sum of all elements in `xs`
   */

  /* First attempt at a recursive solution
  def sum(xs: List[Int]): Int = {
    if (xs.isEmpty)
      0
    else
      xs.head + sum(xs.tail)
  } */

  /* Second attempt at a recursive solution
  def sum(xs: List[Int]): Int =  xs match
  {
    case Nil => 0
    case y :: ys => y + sum(ys)
  } */

  /* Third attempt at a tail recursive solution
  def sum(xs: List[Int]): Int = {
    @tailrec
    def sum_helper(acc: Int, nums: List[Int]): Int = nums match {
      case Nil => acc
      case y :: ys => sum_helper(acc + y, ys)
    }
   sum_helper(0, xs)
  } */

  /* Fourth attempt this time using a fold
  def sum(xs: List[Int]): Int = xs.fold(0)((acc,element) => acc+element)
  */

  /* Final answer, which is very close to the actual definition of sum
  def sum[B >: A](implicit num: Numeric[B]): B = foldLeft(num.zero)(num.plus)*/
  def sum(xs: List[Int]): Int = xs.fold(0)(_ + _)

  /**
   * This method returns the largest element in a list of integers. If the
   * list `xs` is empty it throws a `java.util.NoSuchElementException`.
   *
   * You can use the same methods of the class `List` as mentioned above.
   *
   * ''Hint:'' Again, think of a recursive solution instead of using looping
   * constructs. You might need to define an auxiliary method.
   *
   * @param xs A list of natural numbers
   * @return The largest element in `xs`
   * @throws java.util.NoSuchElementException if `xs` is an empty list
   */

  /* First attempt again using recursion
  def max(xs: List[Int]): Int = {

    if (xs.isEmpty)
      throw new NoSuchElementException()

    def maxHelper(ys: List[Int]): Int = {
      if(ys.tail.isEmpty)
        ys.head
      else
        math.max(ys.head, maxHelper(ys.tail))
    }
    maxHelper(xs)
  }*/

  /* Second attempt using tail recursion & pattern matching
  def max(xs: List[Int]): Int = {
    @tailrec
    def maxHelper(acc: Int, nums: List[Int]): Int = nums match {
      case Nil => acc
      case y :: ys  => maxHelper(math.max(y, acc), ys)
    }

    if (xs.isEmpty)
      throw new NoSuchElementException()
    else
      maxHelper(xs.head, xs.tail)
  } */

  // Final answer, this is pretty close to the actual definition
  def max(xs: List[Int]): Int = xs.max  //.reduce((currentMax,element) => Math.max(currentMax,element))
}
