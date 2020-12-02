package section4

object  HofsCurries extends App {

  // Can we do crazy stuff?
  // yes, we can here is a funky Higher Order Function (a HOF)
  val superFunction: (Int, (String, (Int => Boolean)) => Int ) => (Int => Int) =
  (x: Int, f: (String, Int => Boolean) => Int) => {
    (y: Int) => f("hi", y => y > x)
  }

  /* What is the type of superFunction?  It is a function
  That takes two parameters:
  Param1 Int
  Param2 A function that takes:
      1 String
      2 A function that takes
        1 an Int
        2 returns a Boolean
      returns an Int

  and returns a function that takes an int and returns an int
   */

  // map, flatMap, and filter are common HOFs

  // Some examples:
  // A function that applies a function n times over a value x
  // nTimes(f, n, x)
  // nTimes(f, 3, x) = f(f(f(x)))  apply f 3 times

  @scala.annotation.tailrec
  def nTimes(f: Int => Int, n: Int, x: Int): Int =
    if(n <= 0) x
    else nTimes(f, n-1, f(x))

  val plusOne = (x:Int) => x + 1

  println(s"nTimes(plusOne, 10, 1) = ${nTimes(plusOne, 10, 1)}")

  // lets move towards currying
  def nTimesBetter(f: Int => Int, n: Int): Int => Int =
    if(n <= 0) (x: Int) => x
    else (x: Int) => nTimesBetter(f, n-1)(f(x))

  print("nTimesBetter(plusOne, 10)(1)" + nTimesBetter(plusOne, 10)(1))

  val plus10 = nTimesBetter(plusOne, 10)
  println("plus10(1) = " + plus10(1))


  // here we do it with currying (and multiple parameter lists)

  @scala.annotation.tailrec
  def nTimesCurried(f: Int => Int, n: Int)(x: Int): Int =
    if(n <= 0) x
  else
      nTimesCurried(f, n-1)(f(x))

  def incr10(x: Int) = nTimesCurried(plusOne, 10)(x)
  val anotherIncr10 = nTimesCurried(plusOne, 10)(_)


  // more curried functions
  val superAdderVersion1: Int => (Int => Int) = (x: Int) => (y: Int) => x + y
  val superAdderVersion2: Int => Int => Int = (x: Int) => (y: Int) => x + y
  val superAdderVersion3 = (x:Int) => (y:Int) => x + y
  val superAdder = superAdderVersion3

  val add3 = superAdder(3)  // this is just y => 3 + y
  println(add3(5))
  println(superAdder(3)(5))

  // again currying but this time via multiple parameter lists
  def curriedFormatter(c: String)(x: Double): String = c.format(x)

  val standardFormat: Double => String = curriedFormatter("%4.2f")
  val preciseFormat: Double => String = curriedFormatter("%10.8f")
  println(standardFormat(Math.PI))
  println(preciseFormat(Math.PI))



  // or again regular currying
  def curriedFormatter2(c: String): Double => String = d => c.format(d)

  val standardFormat2 = curriedFormatter2("%4.2f")
  val preciseFormat2 = curriedFormatter2("%10.8f")
  println(standardFormat2(Math.PI))
  println(preciseFormat2(Math.PI))

}



