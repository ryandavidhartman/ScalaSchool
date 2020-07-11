/*
Exercise 1.4.  Observe that our model of evaluation allows for combinations whose operators are compound expressions.
Use this observation to describe the behavior of the following procedure:

(define (a-plus-abs-b a b)
  ((if (> b 0) + -) a b))
 */

def aPlusAbsB(a: Int, b:Int): Int = {
  def op(x:Int): Int = if(b > 0) x+b else x-b
  op(a)
}
val bob = aPlusAbsB(4, -4)
