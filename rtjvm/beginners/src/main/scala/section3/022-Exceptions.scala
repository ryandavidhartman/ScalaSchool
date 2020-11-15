package section3

import scala.util.Try

object Exceptions extends App {

  /*
  Part One Throwing Exceptions
   */

  val x: String = null
  //println(x.length)  this will throw a Null pointer exception

  // throw new NullPointerException this will also throw a  Null pointer  exception

  // A thrown exception is of type Nothing, so this is a subtype of every other type
  //val aWeirdValue:String = throw new NullPointerException

  //throwable classes extend the Throwable class
  //Exception and Error are the major Throwable subtypes
  // Exceptions are used for problems with your program  (e.g. divide by zero)
  // Errors are used for problems with the system. (e.g. out of memory)

  /*
  Part Two Catching Exceptions
   */

  def getInt(withExceptions: Boolean): Int =
    if(withExceptions)
      throw new RuntimeException("No int for you!")
    else
      42

  // Java style
  // If the pattern match misses in the catch, the program will crash
  val mytry: Int = try  {
    getInt(true)
  } catch {
    case _: RuntimeException => println("caught a Runtime exception in a try"); 101
  } finally {
    // code that will get executed no matter what
    println("finally")
  }

  println(s"myTry: $mytry")

  // Scala style
  // If the pattern match misses we'll get a "failed" Try
  val myTry: Try[Int] = Try {
    getInt(true)
  }.recover {
    case _: RuntimeException => println("caught a Runtime exception in a Try"); 101
  }

  println(s"myTry: $myTry")

}
