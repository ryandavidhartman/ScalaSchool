/*
Exercise 1.17.  The exponentiation algorithms in this section are based on performing exponentiation by means of
repeated multiplication. In a similar way, one can perform integer multiplication by means of repeated addition.

The following multiplication procedure (in which it is assumed that our language can only add, not multiply) is
analogous to the expt procedure:

(define (* a b)
  (if (= b 0)
      0
      (+ a (* a (- b 1)))))

This algorithm takes a number of steps that is linear in b.
Now suppose we include, together with addition,
operations double, which doubles an integer, and halve, which divides an (even) integer by 2. Using these,
design a multiplication procedure analogous to fast-expt that uses a logarithmic number of steps.
 */


@scala.annotation.tailrec
def  multiply1(a: Int, b:Int, acc:Int = 0): Int =
if (b == 0)
  acc
else multiply1(a, b-1, acc+a)

multiply1(7, 5)

def doubler(n:Int): Int = n*2
def halfer(n:Int): Int = n/2
def isEven(n:Int) = n %2 == 0

@scala.annotation.tailrec
def  multiply2(a: Int, b:Int, acc:Int = 0): Int =
  if (b == 0)
    acc
  else if(isEven(b))
    multiply2(doubler(a),halfer(b),acc)
  else multiply1(a, b-1, acc+a)

multiply2(7,8)