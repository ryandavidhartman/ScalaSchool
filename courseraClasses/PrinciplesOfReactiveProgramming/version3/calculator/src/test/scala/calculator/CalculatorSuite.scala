package calculator

import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

import org.scalatest._

import TweetLength.MaxTweetLength

@RunWith(classOf[JUnitRunner])
class CalculatorSuite extends FunSuite with ShouldMatchers {

  /******************
   ** TWEET LENGTH **
   ******************/

  /*
  def tweetLength(text: String): Int =
    text.codePointCount(0, text.length)

  test("tweetRemainingCharsCount with a constant signal") {
    val result = TweetLength.tweetRemainingCharsCount(Var("hello world"))
    assert(result() == MaxTweetLength - tweetLength("hello world"))

    val tooLong = "foo" * 200
    val result2 = TweetLength.tweetRemainingCharsCount(Var(tooLong))
    assert(result2() == MaxTweetLength - tweetLength(tooLong))
  }

  test("tweetRemainingCharsCount with a supplementary char") {
    val result = TweetLength.tweetRemainingCharsCount(Var("foo blabla \uD83D\uDCA9 bar"))
    assert(result() == MaxTweetLength - tweetLength("foo blabla \uD83D\uDCA9 bar"))
  }


  test("colorForRemainingCharsCount with a constant signal") {
    val resultGreen1 = TweetLength.colorForRemainingCharsCount(Var(52))
    println(resultGreen1())
    assert(resultGreen1() == "green")
    val resultGreen2 = TweetLength.colorForRemainingCharsCount(Var(15))
    println(resultGreen2())
    assert(resultGreen2() == "green")

    val resultOrange1 = TweetLength.colorForRemainingCharsCount(Var(12))
    println(resultOrange1())
    assert(resultOrange1() == "orange")
    val resultOrange2 = TweetLength.colorForRemainingCharsCount(Var(0))
    println(resultOrange2())
    assert(resultOrange2() == "orange")

    val resultRed1 = TweetLength.colorForRemainingCharsCount(Var(-1))
    println(resultRed1())
    assert(resultRed1() == "red")
    val resultRed2 = TweetLength.colorForRemainingCharsCount(Var(-5))
    println(resultRed2())
    assert(resultRed2() == "red")
  }


  test("test calculator") {
    val a = Literal(1)
    val b = Plus(Ref("a"), Literal(5))
    val c = Divide(Literal(10), Literal(5))
    val d = Times(Literal(10), Literal(5))
    val e = Minus(Literal(10), Literal(5))

    //println("SignalB=" + b())

    val computed: Map[String, Signal[Double]] = Calculator.computeValues(Map[String, Signal[Expr]](
            ("a", Signal(a)),
            ("b", Signal(b)),
            ("c", Signal(c)),
            ("d", Signal(d)),
            ("e", Signal(e))
    ))

    for((k,v) <- computed)  {
      println("k=" + k + ", v=" + v())
    }


  }


  test("test calculator for circular") {
    val a = Plus(Ref("b"), Literal(1))
    val b = Plus(Ref("a"), Literal(5))
    val c = Plus(Literal(2), Literal(1))
    val d = Plus(Ref("c"), Literal(1))
    val e = Plus(Ref("b"), Literal(1))

    val x = Double.NaN
    val testedNAN = x == Double.NaN

    println(testedNAN)

    //println("SignalB=" + b())

    val computed: Map[String, Signal[Double]] = Calculator.computeValues(Map[String, Signal[Expr]](
      ("a", Signal(a)),
      ("b", Signal(b)),
      ("c", Signal(c)),
      ("d", Signal(d)),
      ("e", Signal(e))
    ))

    for((k,v) <- computed)  {
      println("k=" + k + ", v=" + v())
    }


  }



  test("Test Polynomial") {
    val a = Signal(1.0)
    val b = Signal(3.0)
    val c = Signal(1.0)

    val delta = Polynomial.computeDelta(a,b,c)
    println("delta:" + delta())
    val result = Polynomial.computeSolutions(a, b, c, delta)
    println("result:" + result())
  }
*/
  test("Test Depenedency") {
    printThread("Top of Test")
    var a = Var(1.0)
    val b = Var(3.0)
    b() = a() + 1.0
  }

  def printThread(location:String) = {
    val tid = Thread.currentThread().getId
    println(s"At location $location the TID=$tid")
  }
}
