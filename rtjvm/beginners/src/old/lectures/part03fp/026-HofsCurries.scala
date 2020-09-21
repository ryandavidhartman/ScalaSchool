package old.lectures.part03fp

object  HofsCurries extends App {

  // Can we do crazy stuff?
  // yes, we can here is a funky Higher Order Function (a HOF)
  val superFunction: (Int, (String, (Int => Boolean)) => Int ) => (Int => Int) =
  (x: Int, f: (String, Int => Boolean) => Int) => {
    (y: Int) => f("hi", y => y > x)
  }

  /* this is a function
  That takes:
  1 Int
  2 A function that takes:
      1 String
      2 A function takes
        1 an Int
        2 returns a Boolean
      3 returns an Int
  3 returns a function that takes an int and returns an int
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

  println(nTimes(plusOne, 10, 1))

  // lets move towards currying
  def nTimesBetter(f: Int => Int, n: Int): Int => Int =
    if(n <= 0) (x:Int) => x
    else (x:Int) => nTimesBetter(f, n-1)(f(x))

  print(nTimesBetter(plusOne, 10)(1))

  val plus10 = nTimesBetter(plusOne, 10)
  println(plus10(1))


  // here we do it with currying (and multiple parameter lists)

  @scala.annotation.tailrec
  def nTimesCurried(f: Int => Int, n: Int)(x: Int): Int =
    if(n <= 0) x
  else
      nTimesCurried(f, n-1)(f(x))

  def incr10(x: Int) = nTimesCurried(plusOne, 10)(x)
  val anotherIncr10 = nTimesCurried(plusOne, 10)(_)


  // more curried functions
  val superAdderVersion1: Int => (Int => Int) = (x:Int) => (y:Int) => x + y
  val superAdderVersion2: Int => Int => Int = (x:Int) => (y:Int) => x + y
  val superAdderVersion3 = (x:Int) => (y:Int) => x + y
  val superAdder = superAdderVersion3

  val add3 = superAdder(3)  // this is just y => 3 + y
  println(add3(5))
  println(superAdder(3)(5))

  // again currying with multiple parameter lists
  def curriedFormatter(c: String)(x: Double): String = c.format(x)

  val standardFormat: (Double => String) = curriedFormatter("%4.2f")
  val preciseFormat: (Double => String) = curriedFormatter("%10.8f")
  println(standardFormat(Math.PI))
  println(preciseFormat(Math.PI))



  // or again regular currying
  def curriedFormatter2(c: String): (Double) => String = (d: Double) => c.format(d)

  val standardFormat2 = curriedFormatter2("%4.2f")
  val preciseFormat2 = curriedFormatter2("%10.8f")
  println(standardFormat2(Math.PI))
  println(preciseFormat2(Math.PI))

  // Exercises
  // Expand MyList to include a foreach method T => Unit
  // [1,2,3].foreach(x => println(x)
  // see exercises/part3fp/GenericList5.scala

  // Expand MyList to include a sort function ((A, A) => Int) => MyList
  // [1,2,3].sort((x,y) => y - x) => [3,2,1]
  // see exercises/part3fp/GenericList5.scala

  // Expand MyList to include a zipWith (list, (A, A) => B => MyList[B]
  // [1,2,3].zipWith[4,5,6], x*y) => [1*4, 2*5, 3*6]
  // see exercises/part3fp/GenericList5.scala

  // Expand MyList to include a fold. fold(start)(function) => a value
  // [1,2,3].fold(0)(x+y) = 0+1 => 1 (1+2) => 3 (3+3) = 6
  // see exercises/part3fp/GenericList5.scala

  // toCurry(f: (Int, Int) => Int) yields (Int => Int => Int)
  // fromCurry(f: (Int => Int => Int)) yields (Int, Int) => Int
  // compose(f,g) yields  x => f(g(x))
  // andThen(f,g) yields x => g(f(x))
  // see lectures/part3fp/026-HofsCurries.scala


}



