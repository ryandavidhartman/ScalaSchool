import scala.annotation.tailrec

/* Exercise 1.35.

Show that the golden ratio phi (section 1.2.2) is a fixed point of the transformation x => 1 + 1/x,
and use this fact to compute  by means of the fixed-point procedure.
 */

//  phi^2 = phi =1   so phi = 1 + 1/phi

def abs[A](x: A)(implicit num: Numeric[A]) = {
  if (num.lt(x, num.zero)) num.negate(x)
  else x
}


def average[A](x: A, y: A)(implicit num: Numeric[A]) =
  num.toDouble(num.plus(x, y)) / 2.0

def closeEnough(x: Double, y: Double) = abs[Double](x - y) < 0.001

@tailrec
def search(f: Double => Double, negPoint: Double, posPoint: Double): Double = {
  val midPoint = average[Double](negPoint, posPoint)
  if (closeEnough(negPoint, posPoint)) midPoint
  else f(midPoint) match {
    case x if x > 0.0 => search(f, negPoint, midPoint)
    case x if x < 0.0 => search(f, midPoint, posPoint)
    case _ => midPoint
  }
}

val tolerance = 0.00001

def fixedPoint(f: Double => Double, firstGuess: Double): Double = {
  def closeEnough(v1: Double, v2: Double) = abs(v1 - v2) < tolerance
  @tailrec def tryGuess(guess: Double): Double = {
    val next = f(guess)
    if (closeEnough(guess, next)) next
    else tryGuess(next)
  }
  tryGuess(firstGuess)
}

fixedPoint( (x:Double) => 1 + 1.0/x, 1.0)




