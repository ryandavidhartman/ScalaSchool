package part1recap

object GeneralRecap extends App {
  val aCondition: Boolean = false //how to declare an immutable val
  var aVariable = 1  //how to declare a variable, reassignment is allowed
  aVariable = 2

  // expressions
  val aConditionalVal = if(aCondition) 42 else 65

  // code block
  val aCodeBlock = {
    if(aCondition) 74
    56  // this is the value returned by the expression that is the code block
  }

  //types
  // Unit
  val theUnit:Unit = println("Hello, Scala") //Unit is the type of something that just has side effects

  def aFunction(x:Int): Int = x + 1
  val aAnotherFunction: (Int) => Int = { x => x+1}

  val test = aAnotherFunction(5)
  println(test)

  //recursion & tail recursion

  @scala.annotation.tailrec
  def factorial(n:Int, acc:Int = 1): Int =
    if(n <= 0) acc
    else factorial(n-1, acc*n)

}
