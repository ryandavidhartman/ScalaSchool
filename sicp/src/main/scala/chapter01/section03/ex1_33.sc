/*
Exercise 1.33.

You can obtain an even more general version of accumulate (exercise 1.32) by introoducing the notion of a filter on the
terms to be combined. That is, combine only those terms derived from values in the range that satisfy a specified
condition.

The resulting filtered-accumulate abstraction takes the same arguments as accumulate, together with an additional
predicate of one argument that specifies the filter. Write filtered-accumulate as a procedure.


Show how to express the following using filtered-accumulate:

a. the sum of the squares of the prime numbers in the interval a to b
(assuming that you have a prime? predicate already written)

b. the product of all the positive integers less than n that are relatively prime to n
(i.e., all positive integers i < n such that GCD(i,n) = 1).
 */

def accumulate[T,U](
  filter: T=>Boolean,
  combiner: (U,U)=>U,
  null_value: U,
  term: T=>U,
  next: T=>T,
  a: T,
  b: T)(implicit numU: Numeric[U], numT: Numeric[T]): U = {
  @scala.annotation.tailrec
  def acc_helper(acc:U, currentStep:T):U = {
    if (numT.gt(currentStep, b))
      acc
    else {
      if(filter(currentStep))
        acc_helper(combiner(term(currentStep), acc), next(currentStep))
      else acc_helper(acc, next(currentStep))
    }
  }

  acc_helper(null_value, a)
}


// Part A) The sum of the squares of the prime numbers in the Longerval a to b (assuming that you have a prime?
// predicate already written)

def even(n:Long): Boolean = n%2 == 0
def square(n:Long): Long = n*n

def expMod(base:Long, exp: Long, m: Long): Long = exp match {
  case 0 => 1
  case i if even (i) => square (expMod (base, i / 2, m) ) % m
  case j => (base * expMod (base, j - 1, m) ) % m
}

expMod(5,2,3)

def myRandom(n:Long): Long = {
  if(n <= 1) 0 else {
    val r = scala.util.Random
    r.setSeed(System.nanoTime)
    r.nextLong(n)
  }
}

def fermat_test(n:Long): Boolean = {
  def try_it(a:Long): Boolean = expMod(a,n,n) == a
  try_it (1 +  myRandom(n-1))
}

fermat_test(22)

@scala.annotation.tailrec
def fast_prime(n:Long, times:Long): Boolean =
  if (times == 0)
    true
  else if (fermat_test(n))
    fast_prime(n, times- 1)
  else
    false

def isNotPrime(n:Long) = !fast_prime(n,20)
def isPrime(n:Long) = fast_prime(n,20)

def sumPrimeSquares(a:Long, b:Long):Long =
  accumulate[Long, Long](
    filter = isPrime,
    combiner = (x,y)=> x+y,
    null_value = 0L,
    term = square,
    next = x=>x+1,
    a = a,
    b = b)

sumPrimeSquares(1,5)

//Part b) The product of all the positive integers less than n that are relatively prime to n
// (i.e., all positive integers i < n such that GCD(i,n) = 1)

@scala.annotation.tailrec
def gcd(a:Long, b:Long):Long =
  if (b == 0)
    a
  else
    gcd(b, a % b)

def partB(n: Long): Long =
  accumulate[Long, Long](
    filter = x => gcd(x,n) == 1,
    combiner = (x,y)=> x*y,
    null_value = 1L,
    term = square,
    next = x=>x+1,
    a = 1,
    b = n-1)


partB(5)





