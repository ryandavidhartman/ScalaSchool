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
