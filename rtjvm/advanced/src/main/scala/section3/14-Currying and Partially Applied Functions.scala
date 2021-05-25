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
  def curriedMethodAdder(x: Int)(y: Int): Int = x + y


  val cAdd1: Int => Int = curriedMethodAdder(1)
  println(s"cAdd1(3) = ${cAdd1(3)}")

  val cAdd1_noTypeAnnotation = curriedMethodAdder(1)(_)  // Here we need to manually supply a "fake" parameter list
                                                   // when we don't supply the type annotation.
  println(s"cAdd1_noTypeAnnotation(3) = ${cAdd1_noTypeAnnotation(3)}")

  // Under the covers here we are lifting the method curriedAdder into a function literal type.
  // Methods aren't instances of FunctX, they are instances of
  // This is more technically defined as a ETA EXPANSION
  // check out https://stackoverflow.com/questions/39445018/what-is-the-eta-expansion-in-scala

  // The point is the in Scala (and probably in all JVM languages) methods and functions are not the same
  // but methods can be "lifted" via the eta expansion process into functions

  def inc(x: Int): Int = x + 1
  List(1,2,3).map(inc)  // map needs a function inc goes to  x => inc(x)  which is a lambda (function)

  // Partial Function Applications
  val add5OneWay: Int => Int = curriedMethodAdder(5)
  val add5AnotherWay = curriedMethodAdder(5)(_)   // The _ here tells the compiler after we do the ETA expansion
                                                  // we want to return a function literal, not the result of a function

  /*******************************************************************************************
   * EXERCISES
   * Given simpleAddFunction, simpleAddMethod, and curriedAddMethod
   * implement add7: Int => Int = y => 7 + y as many ways as you can think of
   *******************************************************************************************/

  val simpleAddFunction = (x: Int, y: Int) => x + y
  def simpleAddMethod(x: Int, y: Int) = x + y
  def curriedAddMethod(x: Int)(y: Int) = x + y

  val add7_01 = simpleAddFunction(7, _)
  val add7_02 = simpleAddFunction(_, 7)
  val add7_03 = simpleAddFunction.curried(7)
  val add7_04 = (y: Int) => simpleAddFunction(7, y)
  val add7_05 = (y: Int) => simpleAddFunction(y, 7)
  val add7_06: Int => Int = simpleAddFunction(7, _)
  val add7_07: Int => Int = simpleAddFunction(_, 7)
  val add7_08: Int => Int = y => simpleAddFunction(7, y)
  val add7_09: Int => Int = y => simpleAddFunction(y, 7)
  val add7_10: Int => Int = (y: Int) => simpleAddFunction(7, y)
  val add7_11: Int => Int = (y: Int) => simpleAddFunction(y, 7)

  val add7_12 = simpleAddMethod(7, _)
  val add7_13 = simpleAddMethod(_, 7)
  val add7_14 = (y: Int) => simpleAddMethod(7, y)
  val add7_15 = (y: Int) => simpleAddMethod(y, 7)
  val add7_16: Int => Int = simpleAddMethod(7, _)
  val add7_17: Int => Int = simpleAddMethod(_, 7)
  val add7_18: Int => Int = y => simpleAddMethod(7, y)
  val add7_19: Int => Int = y => simpleAddMethod(y, 7)
  val add7_20: Int => Int = (y: Int) => simpleAddMethod(7, y)
  val add7_21: Int => Int = (y: Int) => simpleAddMethod(y, 7)

  val add7_22 = curriedAddMethod(7)(_)
  val add7_23 = curriedAddMethod(7) _
  val add7_24 = curriedAddMethod(_: Int)(7)
  val add7_25 = (y: Int) => curriedAddMethod(7)(y)
  val add7_26 = (y: Int) => curriedAddMethod(y)(7)
  val add7_27: Int => Int = curriedAddMethod(7)(_)
  val add7_28: Int => Int = curriedAddMethod(7) _
  val add7_29: Int => Int = curriedAddMethod(_)(7)
  val add7_30: Int => Int = y => curriedAddMethod(7)(y)
  val add7_31: Int => Int = y => curriedAddMethod(y)(7)
  val add7_32: Int => Int = (y: Int) => curriedAddMethod(7)(y)
  val add7_33: Int => Int = (y: Int) => curriedAddMethod(y)(7)

  (1  to 100).foreach { i =>
    if (add7_01(i) != i + 7) throw new Exception("add7_01 breaks at " + i)
    if (add7_02(i) != i + 7) throw new Exception("add7_02 breaks at " + i)
    if (add7_03(i) != i + 7) throw new Exception("add7_03 breaks at " + i)
    if (add7_04(i) != i + 7) throw new Exception("add7_04 breaks at " + i)
    if (add7_05(i) != i + 7) throw new Exception("add7_05 breaks at " + i)
    if (add7_06(i) != i + 7) throw new Exception("add7_06 breaks at " + i)
    if (add7_07(i) != i + 7) throw new Exception("add7_07 breaks at " + i)
    if (add7_08(i) != i + 7) throw new Exception("add7_08 breaks at " + i)
    if (add7_09(i) != i + 7) throw new Exception("add7_09 breaks at " + i)
    if (add7_10(i) != i + 7) throw new Exception("add7_10 breaks at " + i)
    if (add7_11(i) != i + 7) throw new Exception("add7_11 breaks at " + i)
    if (add7_12(i) != i + 7) throw new Exception("add7_12 breaks at " + i)
    if (add7_13(i) != i + 7) throw new Exception("add7_13 breaks at " + i)
    if (add7_14(i) != i + 7) throw new Exception("add7_14 breaks at " + i)
    if (add7_15(i) != i + 7) throw new Exception("add7_15 breaks at " + i)
    if (add7_16(i) != i + 7) throw new Exception("add7_16 breaks at " + i)
    if (add7_17(i) != i + 7) throw new Exception("add7_17 breaks at " + i)
    if (add7_18(i) != i + 7) throw new Exception("add7_18 breaks at " + i)
    if (add7_19(i) != i + 7) throw new Exception("add7_19 breaks at " + i)
    if (add7_20(i) != i + 7) throw new Exception("add7_20 breaks at " + i)
    if (add7_21(i) != i + 7) throw new Exception("add7_21 breaks at " + i)
    if (add7_22(i) != i + 7) throw new Exception("add7_22 breaks at " + i)
    if (add7_23(i) != i + 7) throw new Exception("add7_23 breaks at " + i)
    if (add7_24(i) != i + 7) throw new Exception("add7_24 breaks at " + i)
    if (add7_25(i) != i + 7) throw new Exception("add7_25 breaks at " + i)
    if (add7_26(i) != i + 7) throw new Exception("add7_26 breaks at " + i)
    if (add7_27(i) != i + 7) throw new Exception("add7_27 breaks at " + i)
    if (add7_28(i) != i + 7) throw new Exception("add7_28 breaks at " + i)
    if (add7_29(i) != i + 7) throw new Exception("add7_29 breaks at " + i)
    if (add7_30(i) != i + 7) throw new Exception("add7_30 breaks at " + i)
    if (add7_31(i) != i + 7) throw new Exception("add7_31 breaks at " + i)
    if (add7_32(i) != i + 7) throw new Exception("add7_32 breaks at " + i)
    if (add7_33(i) != i + 7) throw new Exception("add7_33 breaks at " + i)
  }

  // More on partially applied  functions.  Underscores are powerful.

  def concatenator(a: String, b: String, c: String): String = a + b + c

  def insertName = concatenator("Hello, I'm ", _, " how are you?")
  // insertName is s => concatenator("Hello, I'm ", s, " how are you?")

  println(insertName("Ryan"))
  println(s"is insertName a function? ${insertName.isInstanceOf[(String) => String]}")
  println(s"is concatenator a function? ${insertName.isInstanceOf[(String, String, String) => String]}")

}