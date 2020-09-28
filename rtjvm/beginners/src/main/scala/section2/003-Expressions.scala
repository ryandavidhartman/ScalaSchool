package section2

// see https://ryandavidhartman.github.io/ScalaSchool/Basics.html

object Expressions extends App {

  val x = 1 + 2  // 1 + 2 is an Expression.  This expression is evaluated to the value 3 and has type Int
                 // It is then assigned to the val x
  println(x)


  println( 2 + 3 * 4)
  // Some math operators + - * / & | ^ << >> >>> (right shift with zero extension)

  println( 1 == x)
  // Some boolean operators == != > >= < <=

  println(!(1 == x))
  // ! is the unary negation operator

  var aVariable = 2
  aVariable += 3 // also works with -=, *= /= ... side effects on variables

  // Statements/Instructions (DO something ) VS Expressions (VALUE and/or a type)

  // Consider the IF expression
  val aCondition = true
  val aConditionedValue = if(aCondition) 5 else 3
  println("aConditionedValue: " + aConditionedValue)

  // Scala while has loops but often we don't need them
  var i = 0
  while(i < 10) {
    println(i)
    i += 1
  }

  println("fancy: " +( 1 to 10).mkString(","))

  // A while loops *is* an expression.  But it is an expression of type Unit, and there
  // action is done as a side effect

  // side effects: println(), whiles, reassigning to a var are expressions, but of type Unit

  val aWeirdValue: Unit = { aVariable = 3 }
  println(s"Here is a Unit: $aWeirdValue")

  // code blocks
  val aCodeBlock = {
    val y = 2
    val z = y + 1  // y and z are only defined inside this code block

    if(z >2) "hello" else "goodbye"
  }

  // everything inside the { } is a code block.  It is also an expression, it value is the value of
  // the final expression in the code block.
}
