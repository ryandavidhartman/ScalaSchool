import scala.annotation.tailrec

/* Exercise 1.7
Square Root by Newton's method
 */

def abs[A](x: A)(implicit num: Numeric[A]): A = {
  if (num.lt(x, num.zero)) num.negate(x)
  else x
}

def average[A](x: A, y: A)(implicit num: Numeric[A]): Double=
  num.toDouble(num.plus(x, y)) / 2.0

def square[A](x: A)(implicit num: Numeric[A]): A = num.times(x, x)

def sqrt(x: Double):Double = {
  def goodEnough(guess: Double):Boolean =
    abs(square(guess) - x) < 0.001 * x

  def improve(guess: Double) = average(guess, x / guess)

  @tailrec
  def sqrtIter(guess: Double): Double = {
    if (goodEnough(guess)) guess
    else sqrtIter(improve(guess))
  }

  sqrtIter(1.0)
}

sqrt(25)
sqrt(100)
sqrt(15)

sqrt(100000000000000000L)
sqrt(0.00000000000001)
