import scala.annotation.tailrec

/* Exercise 1.3
Define a procedure that takes three numbers as arguments and returns the sum
of the squares
*/

def square[A](a: A)(implicit num: Numeric[A]): A =
  num.times(a,a)

def sumOfSquares[A](x: A, y: A)(implicit num: Numeric[A]) : A =
  num.plus(square(x), square(y))

def greaterTwoSumOfSquares[A](x: A, y: A, z: A)(implicit num: Numeric[A]) = {

  /*  my way
  val args = Vector(x,y,z).sorted.tail.map(i => num.times(i,i))
  num.plus(args(0), args(1))
  */

  if (num.equiv(x, if (num.gt(x, y)) x else y))
    sumOfSquares(x, if (num.gt(y, z)) y else z)
  else
    sumOfSquares(y, if (num.gt(x, z)) x else z)
}

greaterTwoSumOfSquares(1, 2, 3)
greaterTwoSumOfSquares(1, -2, 3)

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
