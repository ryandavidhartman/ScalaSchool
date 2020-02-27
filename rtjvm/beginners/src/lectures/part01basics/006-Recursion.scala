package lectures.part01basics

import scala.annotation.tailrec

object Recursion extends App {

  def factorial(n: Int): Int =
    if(n <= 1) 1
    else {
      println(s"Computing factorial($n)... first need to find factorial(${n-1})")
      val result = n * factorial(n-1)
      println(s"Computed factorial($n)")
      result
    }

  println(s"factorial(10=${factorial(10)}")


  def factorialTailRec(n: Int): BigInt = {

    @tailrec
    def helper(n:Int, acc:BigInt): BigInt =
    if(n <= 1) acc
    else {
      println(s"Computing helper($n, $acc)")
      helper(n-1, acc*n)
    }

    helper(n,1)
  }

  println(s"factorial(10)= ${factorialTailRec(10)}")

}
