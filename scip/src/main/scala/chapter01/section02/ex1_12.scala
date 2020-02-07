package chapter01.section02

import scala.annotation.tailrec
import scala.util.control.TailCalls.{TailRec, done, tailcall}


/*
Exercise 1.12: The following pattern of numbers is called
Pascal’s triangle

           1
         1   1
       1   2   1
     1   3   3   1
   1   4   6   4   1
. . .

The numbers at the edge of the triangle are all 1, and each
number inside the triangle is the sum of the two numbers
above it.

Write a procedure that computes elements of
Pascal’s triangle by means of a recursive process.
   */

object ex1_12 extends App {

  def pascalPrinter(n: Int, p: (Int, Int) => Int): Unit = {
    (0 until n).foreach { r =>
      val colRange = 0 to r
      colRange.foreach { c =>
        val padding: Int = if (c == 0) 2*(n-r) else 4
        val out = p(c, r).toString.reverse.padTo(padding, ' ').reverse
        print(out)
      }
      println()
    }

  }

  def pascal1(c: Int, r: Int): Int = {
    if (c == 0 || (c == r)) 1
    else
      pascal1(c - 1, r - 1) + pascal1(c, r - 1)
  }

  def pascal2(col: Int, row: Int): Int = {

    val l = if (col > row / 2) col else row - col

    @tailrec
    def helper(i: Int, acc: Int): Int = {
      if (i == l + 1) acc
      else helper(i + 1, acc * (row - l + i) / i)
    }

    helper(1, 1);
  }


  def pascal3(c: Int, r: Int): Int = {
    def helper(c: Int, r: Int): TailRec[Int] =
      if (c == 0 || (c == r)) done(1)
      else for {
        x <- tailcall(helper(c - 1, r - 1))
        y <- tailcall(helper(c, r - 1))
      } yield (x + y)

    helper(c, r).result
  }


  def fact(n: Int): Int = {
    @scala.annotation.tailrec
    def helper(n:Int, acc:Int): Int = if(n <=1) acc else helper(n-1, n*acc)
    helper(n, 1)
  }

  def pascal4(c: Int, r:Int): Int = fact(r) / (fact(c) * fact(r-c))

  /*pascalPrinter(3, pascal1)
  pascalPrinter(3, pascal2)
  pascalPrinter(4, pascal3)*/
  pascalPrinter(4, pascal4)
}
