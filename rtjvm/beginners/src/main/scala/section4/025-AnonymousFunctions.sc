/**
 * Exercises
 * 1 -> Replace all FunctionX calls with lambdas in the Generic List
 *      See 025-Generic List 5.scala
 * 2 -> rewrite the "special" adder as an anonymous function
 *
 **/


// rewrite the "special" adder as an anonymous function


val specialAdder: Function1[Int, Function1[Int, Int]]  = new Function[Int, Function1[Int, Int]] {
  def apply(i: Int): Function1[Int, Int] = new Function[Int, Int] {
    def apply(j: Int): Int = {
      i + j
    }
  }
}

val anonymousAdder: Int => Int => Int = (i:Int) => (j:Int) => i + j

specialAdder(4)(6)
anonymousAdder(4)(6)

val add5 = anonymousAdder(5)
add5(4)

val niceAnonymousAdder1 = (i:Int) => (j:Int) => i + j
val lessNiceAnonymousAdder2: Int => Int => Int = i => _ + i
