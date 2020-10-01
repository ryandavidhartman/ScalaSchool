package section2

// https://ryandavidhartman.github.io/ScalaSchool/Basics.html

object ValuesVariablesTypes extends App {
  val x:Int = 42
  println(x)
  // x = 2 throws a reassignment to val error.  vals are immutable

  val x2 = 42
  // specifying the type is optional.  The compiler figures out the type from type inference
  // val x3: String = 42 won't compile


  //some types

  val aBoolean: Boolean = true
  val aChar: Char = 'x'
  val anInt: Int = x
  val aShort: Short = 1;   // Short.MaxValue is the biggest short
  val aLong: Long = 1000L  // use the L to tell the compiler you want a Long not an Int
  val aDouble: Double = 2.0
  val aFloat: Float = 2.0f // use the f to tell the compiler you want a Float not a Double

  // variables
  var aVariable: Int = 5

  aVariable = 6 // here we are able to reassign data to a variable via a side effect.

  // In general we prefer vals to vars.  This is a major concept in functional programming

}
