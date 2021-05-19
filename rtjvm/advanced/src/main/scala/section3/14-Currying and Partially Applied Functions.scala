package section3

object CurriesPAF extends App {

  // curried functions: A function returning another function that might return another function, but every returned
  // function must take only one parameter at a time

  // Example
  // This is a function that takes an integer parameter, and returns a function that takes an integer
  // which returns an integer!  This is a curried function.  (i.e. it has multiple parameter lists)
  val supperAdder: Int => Int => Int =
  x => y => x + y // Here is an an example implementation of that.

  val add1 = supperAdder(1)
  println(s"add1(3) = ${add1(3)}")
  val add11 = supperAdder(11)
  println(s"add11(3) = ${add11(3)}")

  // Here is the same with a partial function application.  Partially Applied functions also have multiple
  // parameter lists but they can possibly have multiple parameters.
  def curriedAdder(x: Int)(y: Int): Int = x + y

  val cAdd1 = curriedAdder(1)(_)
  println(s"caAd1(3) = ${cAdd1(3)}")
  val cAdd11 = curriedAdder(11)(_)
  println(s"cAdd11(3) = ${cAdd11(3)}")





}