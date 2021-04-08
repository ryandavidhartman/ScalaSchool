package section3

import scala.util.{Failure, Try}

object PartialFunctions extends App {

  val aNormalFunction = (x: Int) => x + 1  // Function1[Int,Int] or the sugared type Int => Int

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

  assert(aPartialFunction(5) == 25)

  val goForIt = Try {
    aPartialFunction(11)
  } match {
    case Failure(e) => println(s" Calling a PF out of range gives a:  $e")
  }

  // Note fussyFunction1 and fussyFunction2 are proper functions, that just happen to
  // below up for certain inputs. The are of type Function1[Int, Int]

  // aPartialFunction is of type PartialFunction[Int, Int]
  // PartialFunction is a subtype of Function1

  //////////////////////////////////////////////////////////
  // SOME PARTIAL FUNCTION UTILITIES
  //////////////////////////////////////////////////////////

  // isDefinedAt will test to see if the partial function is defined for that part of
  // the domain.

  assert(aPartialFunction.isDefinedAt(1) == true)
  assert(aPartialFunction.isDefinedAt(12) == false)

  // You can "lift" a partial function to a regular function that returns an option:

  val liftedFunction = aPartialFunction.lift

  assert(liftedFunction(12) == None)
  assert(liftedFunction(5) == Option(25))

  // Partial Functions can be "chained" with orElse
  val superChain = aPartialFunction.orElse[Int, Int] {
    case 101 => 202
  }
  assert(superChain(101) == 202)

  // Since Partial Functions extend normal Functions we are do this:

  val aTotalFunction: Int => Int = aPartialFunction

  val aSecondFunction: Int => Int = {
    case 1 => 99
  }

  // This means anything that accepts a HOF will also accept a partial function!
  val aMappedList  = List(1, 2, 3) map {
    case 1 => 42
    case 2 => 43
    case 3 => 44
  }
  assert(aMappedList == List(42, 43, 44))

  // quick side note: this blows up:

  Try {
    List(1, 2, 3, 4) map {
      case 1 => 42
      case 2 => 43
      case 3 => 44
    }
  } match {
    case Failure(e) => println(e)
  }

  // Use COLLECT, collect is just like map but it expects a Partial Function
  // and just ignores values outside the PF's domain!

  val collected = List(1, 2, 3, 4) collect {
    case 1 => 42
    case 2 => 43
    case 3 => 44
  }
  assert(collected == aMappedList )


}