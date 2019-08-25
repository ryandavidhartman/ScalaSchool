package reductions

import scala.annotation._
import org.scalameter._
import common._

object ParallelParenthesesBalancingRunner {

  @volatile var seqResult = false

  @volatile var parResult = false

  val standardConfig = config(
    Key.exec.minWarmupRuns -> 40,
    Key.exec.maxWarmupRuns -> 80,
    Key.exec.benchRuns -> 120,
    Key.verbose -> true
  ) withWarmer(new Warmer.Default)

  def main(args: Array[String]): Unit = {
    val length = 100000000
    val chars = new Array[Char](length)
    val threshold = 10000
    val seqtime = standardConfig measure {
      seqResult = ParallelParenthesesBalancing.balance(chars)
    }
    println(s"sequential result = $seqResult")
    println(s"sequential balancing time: $seqtime ms")

      val fjtime = standardConfig measure {
      parResult = ParallelParenthesesBalancing.parBalance(chars, threshold)
    }
    println(s"parallel result = $parResult")
    println(s"parallel balancing time: $fjtime ms")
    println(s"speedup: ${seqtime / fjtime}")
  }
}

object ParallelParenthesesBalancing {

  /** Returns `true` iff the parentheses in the input `chars` are balanced.
    */
  def balance(chars: Array[Char]): Boolean = {
    def code(ch: Char): Int = ch match {
      case '(' => 1
      case ')' => -1
      case _ => 0
    }
    @tailrec
    def loop(chLs: List[Char], acc: Int = 0): Int = chLs match {
      case head::tail if acc >= 0 => loop(tail, acc + code(head))
      case _ => acc
    }
    loop(chars.toList) == 0
  }

  /** Returns `true` iff the parentheses in the input `chars` are balanced.
    */
  def parBalance(chars: Array[Char], threshold: Int): Boolean = {

    def traverse(idx: Int, until: Int, arg1: Int, arg2: Int): (Int, Int) = {
      if (idx < until) {
        chars(idx) match {
          case '(' => traverse(idx + 1, until, arg1 + 1, arg2)
          case ')' =>
            if (arg1 > 0) traverse(idx + 1, until, arg1 - 1, arg2)
            else traverse(idx + 1, until, arg1, arg2 + 1)
          case _ => traverse(idx + 1, until, arg1, arg2)
        }
      } else (arg1, arg2)
    }

    def reduce(from: Int, until: Int): (Int, Int) = {
      val size = until - from
      if (size > threshold) {
        val halfSize = size / 2
        val ((a1, a2), (b1, b2)) = parallel(reduce(from, from + halfSize), reduce(from + halfSize, until))
        if (a1 > b2) {
          // )))((())(( => )))(((
          (a1 - b2 + b1) -> a2
        } else {
          // )))(()))(( => ))))((
          b1 -> (b2 - a1 + a2)
        }
      }
      else {
        traverse(from, until, 0, 0)
      }
    }

    reduce(0, chars.length) == (0, 0)
  }

  // For those who want more:
  // Prove that your reduction operator is associative!

}

/*
package reductions


import scala.annotation._
import org.scalameter._
import common._


object ParallelParenthesesBalancingRunner {

  @volatile var seqResult = false

  @volatile var parResult = false

  val standardConfig = config(
    Key.exec.minWarmupRuns -> 40,
    Key.exec.maxWarmupRuns -> 80,
    Key.exec.benchRuns -> 120,
    Key.verbose -> true
  ) withWarmer(new Warmer.Default)

  def main(args: Array[String]): Unit = {
    val length = 10000
    val chars = new Array[Char](length)
    val threshold = 10000/3
    val seqtime = standardConfig measure {
      seqResult = ParallelParenthesesBalancing.balance(chars)
    }
    println(s"sequential result = $seqResult")
    println(s"sequential balancing time: $seqtime ms")

    val fjtime = standardConfig measure {
      parResult = ParallelParenthesesBalancing.parBalance(chars, threshold)
    }
    println(s"parallel result = $parResult")
    println(s"parallel balancing time: $fjtime ms")
    println(s"speedup: ${seqtime / fjtime}")
  }
}

object ParallelParenthesesBalancing {

  /** Returns `true` iff the parentheses in the input `chars` are balanced.
   */
  def balance(chars: Array[Char]): Boolean = {
    @tailrec
    def helper(acc:Int, chars: Array[Char]): Boolean = {
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

  /** Returns `true` iff the parentheses in the input `chars` are balanced.
   */
  def parBalance(chars: Array[Char], threshold: Int): Boolean = {

    def traverse(idx: Int, until: Int, unmatchedOpen: Int, unmatchedClose: Int): (Int, Int) = {

      @tailrec
      def helper(data: Array[Char], acc: (Int, Int)): (Int, Int) = {
        if(data.isEmpty) acc
        else if(data.head == '(') helper(data.tail, (acc._1 +1,  acc._2))
        else if (data.head == ')')  {
          if(acc._1 > 0) helper(data.tail, (acc._1-1, acc._2))
          else helper(data.tail, (acc._1, acc._2+1))
        }
        else helper(data.tail, acc)
      }

      helper(chars.slice(idx, until), (unmatchedOpen,unmatchedClose))
    }


    def reduce(from: Int, until: Int): (Int, Int) = {
      if((until - from) <= threshold) traverse(from, until, 0, 0)
      else {
        val mid = (until - from) / 2
        val ((a1, a2), (b1, b2)) = parallel(reduce(from, mid), reduce(mid, until))
        if (a1 > b2) {
          // )))((())(( => )))(((
          (a1 - b2 + b1) -> a2
        } else {
          // )))(()))(( => ))))((
          b1 -> (b2 - a1 + a2)
        }
      }
    }

    reduce(0, chars.length) == (0,0)
  }

  implicit class TuppleAdd(t: (Int, Int)) {
    def +(p: (Int, Int)) = (p._1 + t._1, p._2 + t._2)
  }

  // For those who want more:
  // Prove that your reduction operator is associative!

}
*/
