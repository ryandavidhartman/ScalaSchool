package lectures.part01basics

object Functions extends App {

  //How to define a function
  def aFunction(a:String, b:Int): String = a + " " + b

  println(aFunction("hello", 3))

  // Note parameterless functions can be called without ()
  def aParameterlessFuncton(): Int = 42
  println(aParameterlessFuncton())
  println(aParameterlessFuncton)

 //How can we do loops in a more functional way?  (i.e. avoid using loops
  def aRepeatedFunction(aString: String, n: Int): String = {
    if(n == 1) aString
    else aString + aRepeatedFunction(aString, n-1)
  }

  def aRepeatedFunction2(aString: String, n: Int): String = n match {
    case 1 => aString
    case _ => aString + aRepeatedFunction(aString, n-1)
  }

  def aRepeatedFunction3(aString: String, n: Int): String = {
    @scala.annotation.tailrec
    def helper(n: Int, acc: String): String = {
      if (n == 0) acc
      else helper(n - 1, aString + acc)
    }

    helper(n, "")
  }

  def aRepeatedFunction4(aString: String, n: Int): String = Seq.fill(n)(aString).mkString("")

  println(aRepeatedFunction4("hello", 3))


  def aFunctionWithSideEffects(aString: String): Unit = println(aString)

  def aBigFunction(n:Int): Int = {
    def aSmallerFunction(a:Int, b: Int) = a + b

    aSmallerFunction(n,n)
  }

}
