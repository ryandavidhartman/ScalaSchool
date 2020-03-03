/*
Exercise 1.30.

def sum[T,U](term: T=>U, a:T, next: T=>T, b: T)(implicit numU: Numeric[U], numT: Numeric[T]): U = {
  if (numT.gt(a,b))
    numU.zero
  else
    numU.plus(term(a), sum(term, next(a), next, b))
}


The sum procedure above generates a linear recursion. The procedure can be rewritten so that the sum is
performed iteratively. Show how to do this by filling in the missing expressions in the following definition:

(define (sum term a next b)
  (define (iter a result)
    (if <??>
        <??>
        (iter <??> <??>)))
  (iter <??> <??>))
 */


def cube(x:Double) = x*x*x

def sum[T,U](term: T=>U, a:T, next: T=>T, b: T)(implicit numU: Numeric[U], numT: Numeric[T]): U = {
  @scala.annotation.tailrec
  def sum_helper(acc:U, current_position:T):U =
  if (numT.gt(current_position,b))
    acc
  else
    sum_helper(numU.plus(term(current_position),acc), next(current_position))

  sum_helper(numU.zero,a)
}

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
simpson(cube, 0, 1, 10000)
