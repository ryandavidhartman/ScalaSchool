package section3

import scala.util.{Failure, Try}

object PartialFunctions extends App {

  val aFunction = (x: Int) => x + 1  // Function1[Int,Int] or the sugared type Int => Int

  // In mathematics this function is defined for all x elements of the set of Integers.
  // This is a "regular" function.

  // Partial Functions are functions that aren't applicable for all values.  Examples:

  class FunctionNotApplicableException extends RuntimeException

  val fussyFunction1 = (x: Int) =>
    if(x <= 10 && x >= -10) x*x
    else
      throw new FunctionNotApplicableException

  // Here fussyFunction1 is only applicable for |x| <= 10.  It fails for all other values
  // You can also write this with a pattern match

  val fussyFunction2 = (x: Int) => x match {
    case x1 if x1 >= -10 && x1 <= 10 => x1*x1
  }

  // Now you may also leave off the explicit pattern match and just write:
  val aPartialFunction: PartialFunction[Int, Int] = {
    case x:Int if x >= -10 && x <= 10 => x*x
  }

  println(aPartialFunction(5))

  Try {
    aPartialFunction(11)
  } match {
    case Failure(e) => println(e)
  }

  // Note fussyFunction1 and fussyFunction2 are proper functions, that just happen to
  // below up for certain inputs. The are of type Function1[Int, Int]

  // aPartialFunction is of type PartialFunction[Int, Int]

  //////////////////////////////////////////////////////////
  // SOME PARTIAL FUNCTION UTILITIES
  //////////////////////////////////////////////////////////

  // isDefinedAt will test to see if the partial function is defined for that part of
  // the domain.

  println(aPartialFunction.isDefinedAt(1))
  println(aPartialFunction.isDefinedAt(12))







}