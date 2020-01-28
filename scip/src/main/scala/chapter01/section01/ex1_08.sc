import scala.annotation.tailrec

/*
Exercise 1.8

Newton's method for cube roots is based on the fact that if y is an approximation to the cube root of x, then a better
approximation is given by the value

Use this formula to implement a cube-root procedure analogous to the square-root procedure. (In section 1.3.4 we will
see how to implement Newton's method in general as an abstraction of these square-root and cube-root procedures.)

*/

def abs[A](x: A)(implicit num: Numeric[A]): A = {
  if (num.lt(x, num.zero)) num.negate(x)
  else x
}


def toThePowerOf[A](x: A, n: Int)(implicit num: Numeric[A]): A = {
  assert(n>0)
  @tailrec
  def helper(acc:A, n:Int): A =
    if (n == 1) acc
    else helper(num.times(acc,x), n-1)
  helper(x,n)
}
def square[A](x: A)(implicit num: Numeric[A]): A = toThePowerOf(x,2)
def cube[A](x: A)(implicit num: Numeric[A]): A =toThePowerOf(x,3)

def cubeRoot(x: Double):Double = {
  def goodEnough(guess: Double):Boolean =
    abs(cube(guess) - x) < 0.0001 * x

  def improve(guess: Double) = (x/square(guess) + 2*guess)/3.0

  @tailrec
  def cubeRootIter(guess: Double): Double = {
    if (goodEnough(guess)) guess
    else cubeRootIter(improve(guess))
  }

  cubeRootIter(1.0)
}


cubeRoot(1)
cubeRoot(8)
cubeRoot(13*13*13)
cubeRoot(100000000L)


