package section2

object StringOps extends App {

  val str: String = "Hello, I am learning Scala"

  // We get all of the Java String functions
  println(str.charAt(2))
  println(str.substring(7,11))
  println(str.split(" ").toList)
  println(str.startsWith("Hello"))
  println(str.replace(" ",  "-"))
  println(str.toLowerCase)
  println(str.toUpperCase)
  println(str.length)

  // There are some Scala functions too
  val aNumberString = "45"
  val aNumber = aNumberString.toInt

  println('a' +: aNumberString :+ 'z' )
  println(str.reverse)
  println(str.take(2))

  // S-interpolators
  val name = "David"
  val age = 12
  val greeting = s"Hello, my name is $name and I am $age years old"
  val greeting2 = s"Hello, my name is $name and I am ${age+1} years old"

  // F-interpolators
  val speed = 1.2f
  val myth = f"$name can east $speed%2.2f burgers per minute"
  println(myth)

  //raw-interpolator
  println(raw"This is a newline: \n")
}
