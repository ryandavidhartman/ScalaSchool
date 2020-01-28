import scala.annotation.tailrec

/*
Excerise 1.7
The good-enough? test used in computing square roots will not be very effective for
finding the square roots of very small numbers. Also, in real computers, arithmetic
operations are almost always performed with limited precision. This makes our test
inadequate for very large numbers. Explain these statements, with examples showing
how the test fails for small and large numbers. An alternative strategy for implementing
good-enough? is to watch how guess changes from one iteration to the next and to stop when
the change is a very small fraction of the guess. Design a square-root procedure that uses
this kind of end test. Does this work better for small and large numbers?
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
