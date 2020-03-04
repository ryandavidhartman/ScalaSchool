/*
Exercise 1.33.

You can obtain an even more general version of accumulate (exercise 1.32) by introducing the notion of a filter on the
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
  filter: U=>Boolean,
  combiner: (U,U)=>U,
  null_value: U,
  term: T=>U,
  next: T=>T,
  a: T,
  b: T)(implicit numU: Numeric[U], numT: Numeric[T]): U = {
  @scala.annotation.tailrec
  def acc_helper(acc:U, nextStep:T):U = {
    if (numT.gt(nextStep, b))
      acc
    else {
      val nextTerm = term(nextStep)
      if(filter(nextTerm))
        acc_helper(combiner(term(nextStep), acc), next(nextStep))
      else acc_helper(acc, next(nextStep))
    }
  }

  acc_helper(null_value, a)
}

