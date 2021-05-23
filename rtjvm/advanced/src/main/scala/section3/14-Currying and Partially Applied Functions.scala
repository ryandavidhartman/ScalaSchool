package section3

object CurriesPAF extends App {

  // curried functions: A function returning another function that might return another function, but every returned
  // function must take only one parameter at a time

  // Example
  // This is a function that takes an integer parameter, and returns a function that takes an integer
  // which returns an integer!  This is a curried function.  (i.e. it has multiple parameter lists)
  val supperAdder: Int => Int => Int =
  x => y => x + y // Here is an an example implementation of that.

  val add1 = supperAdder(1)  // Int => Int = y => 1 + y
  println(s"add1(3) = ${add1(3)}")
  val add11 = supperAdder(11)
  println(s"add11(3) = ${add11(3)}")

  // Note we can also call supperAdder and get a value by supplying 2 parameter lists

  println(s"supperAdder(4)(3): ${supperAdder(4)(3)}")

  // In Scala we can also create curried methods (again methods that take multiple parameter lists)
  def curriedAdder(x: Int)(y: Int): Int = x + y


  val cAdd1: Int => Int = curriedAdder(1)
  println(s"cAdd1(3) = ${cAdd1(3)}")

  val cAdd1_noTypeAnnotation = curriedAdder(1)(_)  // Here we need to manually supply a "fake" parameter list
                                                   // when we don't supply the type annotation.
  println(s"cAdd1_noTypeAnnotation(3) = ${cAdd1_noTypeAnnotation(3)}")

  // Under the covers here we are lifting the method curriedAdder into a function literal type
  // This is more technically defined as a ETA EXPANSION
  // check out https://stackoverflow.com/questions/39445018/what-is-the-eta-expansion-in-scala

  // The point is the in Scala (and probably in all JVM languages) methods and functions are not the same
  // but methods can be "lifted" via the eta expansion process into functions

  def inc(x: Int): Int = x +1
  List(1,2,3).map(inc)  // needs a function inc goes to  x => inc(x)  which is a lambda (function)


}