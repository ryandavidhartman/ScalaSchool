package section4

import scala.util.{Success,Failure,Try}

object HandlingFailure extends App {

  // create a success and a failure manually
  val aSuccess = Success(3)  // Type Try[Int]
  val aFailure: Try[Int] = Failure(new Exception("I failed"))  // Type Try[Nothing]

  println(s"Here is what a Success looks like: $aSuccess")
  println(s"Here is what a Failure looks like: $aFailure")

  // normally we use the Try companion object to instantiate a Success or Failure
  // depending on the result of some computation

  def unsafeMethod(): String = throw new RuntimeException("No string for you!")

  // Try objects via the apply method
  val potentialFailure = Try(unsafeMethod())
  println(s"Notice even though an exception was thrown the program didn't crash. $potentialFailure")

  // some syntactic sugar
  val anotherPotentialFailure = Try {
    //
    // stuff  that could fail!
    //
  }

  // utilities

  // isSuccess and isFailure
  potentialFailure.isSuccess  // test for success or failure

  // orElse and getOrElse
  def backupMethod(): String = "A valid back-up string"
  val stringSuccess =  Try(unsafeMethod()).orElse(Try(backupMethod()))  // if you want to chain another Try
  val stringVal = Try(unsafeMethod()).getOrElse(backupMethod())  // if you want a value

  // If you are the one designing the API the consider returning a Try if your function  might
  // throw an exception or otherwise fail

  def betterUnsafeMethod(): Try[String] = Failure(new RuntimeException)
  val stringSuccess2 =  betterUnsafeMethod.orElse(Try(backupMethod()))  // if you want to chain another Try
  val stringVal2 = betterUnsafeMethod.getOrElse(backupMethod())         // if you want a value

  // Try also has map, flatMap and filter.  So we are able to use For comprehensions
  println(aSuccess.map(_ * 2))  // the map will operation on the values of successfully Try's
  println(aSuccess.flatMap(v => Success(v * 10)))
  println(aSuccess.filter(_  % 2 == 0).map(_ * 2))  //the filter causes the success to turn into a Failure

}
