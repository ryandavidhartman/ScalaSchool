/*
Exercise 1.31.
a.  The sum procedure is only the simplest of a vast number of similar abstractions that can be captured as higher-order
procedures.51 Write an analogous procedure called product that returns the product of the values of a function at points
over a given range. Show how to define factorial in terms of product. Also use product to compute approximations to
using the formula

          2*4*4*6*6*8...
pi/4 =  _____________________
         3*3*5*5*7*7...

b.  If your product procedure generates a recursive process, write one that generates an iterative process. If it
generates an iterative process, write one that generates a recursive process.
 */

def prod[T,U](term: T=>U, a:T, next: T=>T, b: T)(implicit numU: Numeric[U], numT: Numeric[T]): U = {
  if (numT.gt(a,b))
    numU.one
  else
    numU.times(term(a), prod(term, next(a), next, b))
}

def prod_iter[T,U](term: T=>U, a:T, next: T=>T, b: T)(implicit numU: Numeric[U], numT: Numeric[T]): U = {
  @scala.annotation.tailrec
  def prod_helper(acc:U, current_position:T):U =
    if (numT.gt(current_position,b))
      acc
    else
      prod_helper(numU.times(term(current_position),acc), next(current_position))

  prod_helper(numU.one,a)
}


def wallis_pi(n:Int):Double = {

  def term(k:Int):Double = (2.0*k)/((2.0*k)-1.0)*(2.0*k)/(2.0*k+1.0)
  def inc(i:Int): Int = i+1
  prod_iter(term, 1, inc, n)*2.0
}

wallis_pi(100)
wallis_pi(1000)
wallis_pi(10000)
wallis_pi(100000)