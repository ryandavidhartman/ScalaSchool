package ScalaScheme

import ScalaScheme.Primitives._


object SchemeMath {

  val ERROR_TOLERANCE = 0.00001

  def multiply(args: SchemeData*): Double = {
    @scala.annotation.tailrec
    def mHelper[T](acc: Double, rest:Seq[Double]):Double =
      if(rest.isEmpty)
        acc
      else
        mHelper(acc * rest.head, rest.tail)
    mHelper(acc=1.0, args.map(_.toString.toDouble))
  }

  def sum(args: SchemeData*): Double = {
    @scala.annotation.tailrec
    def mHelper[T](acc: Double, rest:Seq[Double]):Double =
      if(rest.isEmpty)
        acc
      else
        mHelper(acc + rest.head, rest.tail)
    mHelper(acc=0.0, args.map(_.toString.toDouble))
  }

  def division(args: SchemeData*): Double = {
    if(args.length == 1)  1.0/ multiply(args)
    else if(args.length > 1) args.head.toString.toDouble / multiply(args.tail:_*)
    else throw new IllegalArgumentException("Division needs at least one number")
  }

  def square(n: SD): SD = multiply(n,n)

  def isDivisor(a: SD, b: SD): Boolean = {
    val left = a.toString.toDouble
    val right = b.toString.toDouble
    Math.abs(left % right) <= (ERROR_TOLERANCE/right)
  }

  def smallestDivisor(n: SD): SD = {
    @scala.annotation.tailrec
    def findDivisor(n: Int, testDivisor: Int): Int = {
      if(testDivisor*testDivisor >= n)
        n
      else if (isDivisor(n, testDivisor))
        testDivisor
      else
        findDivisor(n, testDivisor + 1)
    }

    findDivisor(n.toString.toInt, testDivisor = 2)
  }

  def isPrime(in: SD): Boolean = {
    val d:Double = in.toString.toDouble
    if(d % 1 == 0) {
      val n = d.toInt
      smallestDivisor(n) == n
    } else {
      throw new NumberFormatException(s"$in is not a valid integer")
    }
  }
}
