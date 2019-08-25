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
    assert(r >= 0)
    assert(c <= r)

    if (c == 0 || c == r) 1 else pascal(c - 1, r - 1) + pascal(c, r - 1)
  }

  /**
    * Exercise 2
    */
  def balance(chars: List[Char]): Boolean = {
    @tailrec
    def helper(acc: Int, chars: List[Char]): Boolean = chars match {
      case Nil => acc == 0
      case c :: cs => if (c == '(') helper(acc + 1, cs)
      else if (acc <= 0) false
      else helper(acc - 1, cs)
    }

    helper(0, chars.filter(c => c == '(' || c == ')'))
  }


  /**
    * Exercise 3
    */
  def countChange(money: Int, coins: List[Int]): Int = {
    val sortedCoins = coins.sortWith((l, r) => l >= r)

    def helper(money: Int, index: Int): Int = {
      if (money == 0) 1
      else if (money < 0 || index >= sortedCoins.length) 0
      else helper(money, index + 1) + helper(money - sortedCoins(index), index)
    }

    helper(money, 0)
  }
}
