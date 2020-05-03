/*
The (log n) primality test is based on a result from number theory known as Fermat's Little Theorem.45

Fermat's Little Theorem: If n is a prime number and a is any positive integer less than n, then a raised to the nth
power is congruent to a modulo n.

(Two numbers are said to be congruent modulo n if they both have the same remainder when divided by n. The remainder of
a number a when divided by n is also referred to as the remainder of a modulo n, or simply as a modulo n.)

If n is not prime, then, in general, most of the numbers a<n will not satisfy the above relation. This leads to the
following algorithm for testing primality:

Given a number n, pick a random number a < n and compute the remainder of an modulo n. If the result is not equal to a,
then n is certainly not prime. If it is a, then chances are good that n is prime. Now pick another random number a and
test it with the same method. If it also satisfies the equation, then we can be even more confident that n is prime.

By trying more and more values of a, we can increase our confidence in the result.

This algorithm is known as the Fermat test.

To implement the Fermat test, we need a procedure that computes the exponential of a number modulo another number:
 */

def even(n:Int): Boolean = n%2 == 0
def square(n:Int): Int = n*n

def expMod(base:Int, exp: Int, m: Int): Int = exp match {
  case 0 => 1
  case i if even (i) => square (expMod (base, i / 2, m) ) % m
  case j => (base * expMod (base, j - 1, m) ) % m
}

expMod(5,2,3)

def myRandom(n:Int): Int = {
  val r = scala.util.Random
  r.setSeed(System.nanoTime)
  r.nextInt(n)
}

def fermat_test(n:Int): Boolean = {
  def try_it(a:Int): Boolean = expMod(a,n,n) == a
  try_it (1 +  myRandom(n-1))
}

fermat_test(22)

@scala.annotation.tailrec
def fast_prime(n:Int, times:Int): Boolean =
  if (times == 0)
    true
  else if (fermat_test(n))
    fast_prime(n, times- 1)
  else
    false


(1 to 100).foreach(i => println(myRandom(7)))
fast_prime(7,20)
