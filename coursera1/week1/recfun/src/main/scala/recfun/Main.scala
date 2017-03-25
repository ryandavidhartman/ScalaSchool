package recfun

import scala.annotation.tailrec

object Main {
  def main(args: Array[String]) {
    println("Pascal's Triangle")
    for (row <- 0 to 10) {
      for (col <- 0 to row)
        print(pascal(col, row) + " ")
      println()
    }
  }

  /**
   * Exercise 1
   */
  def pascal(c: Int, r: Int): Int = {
    if(c <= 0 || c >= r)
      1
    else
      pascal(c-1,r-1) + pascal(c,r-1)
  }
  
  /**
   * Exercise 2
   */
  def balance(chars: List[Char]): Boolean = {
    @tailrec
    def helper(acc:Int, chars: List[Char]): Boolean = {
      if(chars.isEmpty) {
        acc == 0
      }
      else {
        val next = acc + (chars.head match {
          case '(' => 1
          case ')' => -1
          case _ => 0
        })

        if(next < 0) false else helper(next, chars.tail)
      }
    }

    helper(0,chars)
  }

    /**
   * Exercise 3
   */
    def countChange(money: Int, coins: List[Int]): Int = {
      val sortedCoins = coins.sorted

      def count(money:Int, index:Int):Int = {
        if (money == 0) 1
        else if(money < 0 || index < 0) 0
        else count(money, index-1) + count(money-sortedCoins(index), index)
      }

      count(money, coins.length-1)
    }

}
