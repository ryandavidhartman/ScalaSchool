import scala.annotation.tailrec

/* Exercise 1.7
Square Root by Newton's method
 */

def abs[A](x: A)(implicit num: Numeric[A]) = {
  if (num.lt(x, num.zero)) num.negate(x)
  else x
}

def average[A](x: A, y: A)(implicit num: Numeric[A]) =
  num.toDouble(num.plus(x, y)) / 2.0


def sqrt(x: Double) = {
  def goodEnough(guess: Double) = abs(square(guess) - x) < 0.001 * x

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
