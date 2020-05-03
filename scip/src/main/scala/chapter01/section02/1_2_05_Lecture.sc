/*
Greatest Common Divisors

The greatest common divisor (GCD) of two integers a and b is defined to be the largest integer that divides both a and
b with no remainder. For example, the GCD of 16 and 28 is 4. In chapter 2, when we investigate how to implement
rational-number arithmetic, we will need to be able to compute GCDs in order to reduce rational numbers to lowest
terms. (To reduce a rational number to lowest terms, we must divide both the numerator and the denominator by their GCD.

 For example, 16/28 reduces to 4/7.) One way to find the GCD of two integers is to factor them and search for common
 factors, but there is a famous algorithm that is much more efficient.

The idea of the algorithm is based on the observation that, if r is the remainder when a is divided by b, then the
common divisors of a and b are precisely the same as the common divisors of b and r. Thus, we can use the equation


to successively reduce the problem of computing a GCD to the problem of computing the GCD of smaller and smaller pairs
of integers. For example,

GCD(206, 40) = GCD(40, 6)
             = GCD(6, 4)
             = GCD(4, 2)
             = GCD(2, 0)
             = 2


reduces GCD(206,40) to GCD(2,0), which is 2. It is possible to show that starting with any two positive integers and
performing repeated reductions will always eventually produce a pair where the second number is 0. Then the GCD is the
other number in the pair. This method for computing the GCD is known as Euclid's Algorithm

It is easy to express Euclid's Algorithm as a procedure:

(define (gcd a b)
  (if (= b 0)
      a
      (gcd b (remainder a b))))

 */

@scala.annotation.tailrec
def gcd(a:Int, b: Int): Int = if(b == 0) a else gcd(b, a % b)

gcd(2323,0)

gcd(100, 15)

gcd(225,15)
