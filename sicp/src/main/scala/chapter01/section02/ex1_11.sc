/*
Exercise 1.11.

A function f is defined by the rule that
f(n) = n if n<3 and
f(n) = f(n - 1) + 2f(n - 2) + 3f(n - 3) if n> 3.

Write a procedure that computes f by means of a recursive process.
Write a procedure that computes f by means of an iterative process.
 */

def f_rec(n:Int): Int =
if(n <3)
  n
else
  f_rec(n-1) + 2*f_rec(n-2) + 3*f_rec(n-3)

f_rec(0)
f_rec(1)
f_rec(2)
f_rec(3)
f_rec(4)
f_rec(5)
f_rec(6)
// f_rec(100) -> blows up!


def f_iter(n:Int): Int = {

  @scala.annotation.tailrec
  def helper(fn_1: Int, fn_2: Int, fn_3: Int, j:Int): Int =
  if(j == 0)
    fn_3
  else
    helper(fn_1 + 2*fn_2 + 3*fn_3, fn_1, fn_2, j-1)

  helper(2, 1, 0, n)
}

f_iter(0)
f_iter(1)
f_iter(2)
f_iter(3)
f_iter(4)
f_iter(5)
f_iter(6)
f_iter(100)




