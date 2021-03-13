package section2

import scala.util.Try

object DarkSugars extends App {
  // #1 Methods with single parameters

  def singleArgMethod(arg: Int): String= arg.toString  // random method that takes a single argument
  // you can leave off the (), since this can be implied

  val someString = singleArgMethod {
    // ...
    // all kinds of code
    // ...
    // that finally returns a int
    2
  }

  // example  a) The Scala Try.apply takes a single argument
  // Try.apply(arg) => Try(arg) since apply methods can be inferred
  // Try.apply(arg) => Try(arg) => Try arg => Try expression => Try { code in block }

  // This means the Scala Try can look just like the Java try

  val aTryInstance = Try {
    // looks like Java try
    // code
    2
    throw new RuntimeException
  }








}