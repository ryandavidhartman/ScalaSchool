/*
Exercise 1.29.

Simpson's Rule is a more accurate method of numerical integration than the method illustrated in:
chapter01/section03/lecture.sc

Using Simpson's Rule, the integral of a function f between a and b is approximated as

h/3 * [y0 + 4y + 2y2 + 4y3 + 2y4 + ... + 2yn-2 + 4yn-1 + 4n]

where

h = (b - a)/n, for some even integer n,
and yk = f(a + kh).

(Increasing n increases the accuracy of the approximation.)

Define a procedure that takes as arguments f, a, b, and n and returns the value of the integral, computed using
Simpson's Rule. Use your procedure to integrate cube between 0 and 1 (with n = 100 and n = 1000), and compare the
results to those of the integral procedure shown in chapter01/section03/lecture.sc
 */

def cube[T](x:T)(implicit num: Numeric[T]):T = num.times(x, num.times(x,x))

def sum[T,U](term: T=>U, a:T, next: T=>T, b: T)(implicit numU: Numeric[U], numT: Numeric[T]): U = {
  if (numT.gt(a,b))
    numU.zero
  else
    numU.plus(term(a), sum(term, next(a), next, b))
}

def integral(f:Double=>Double, a:Double, b:Double, dx:Double): Double =
  sum[Double,Double](f, a, x => x+dx, b)*dx

integral(cube, 0, 1, 0.01)
integral(cube, 0, 1, 0.001)

def simpson(f:Double=>Double, a:Double, b:Double, n:Int):Double = {
  val h = (b-a)/n

  def term(k:Int):Double = {
    val coefficient = k match {
      case 0 => 1.0
      case j if j==n => 1.0
      case k if k%2==0 => 2.0
      case _ => 4.0
    }
    coefficient*f(a+h*k)
  }

  h /3 * sum[Int, Double](term, 0, x=>x+1, n)
}

simpson(cube, 0, 1, 100)
simpson(cube, 0, 1, 1000)