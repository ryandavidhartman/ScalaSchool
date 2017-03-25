package recfun
import common._

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
    if(c == 0)
      1
    else if(c == r)
      1
    else
      pascal(c-1,r-1) + pascal(c,r-1)

  }

  /**
   * Exercise 2
   */
  def balance(chars: List[Char]): Boolean = {

    def loop(acc: Int, chars: List[Char]): Int = {
      if(chars.isEmpty || acc < 0)
        acc
      else {
        val current = chars.head
        val acc2 =
          if(current == '(')
            acc + 1
          else if (current == ')')
            acc - 1
          else
            acc
        loop(acc2, chars.tail)
      }
    }

    loop(0, chars) == 0

  }

  /**
   * Exercise 3
   */
  def countChange(money: Int, coins: List[Int]): Int = {

    if(money == 0)
      1
    else if(money < 0 || coins.isEmpty)
      0
    else
      countChange(money,coins.tail) + countChange(money-coins.head, coins)

  }

}
