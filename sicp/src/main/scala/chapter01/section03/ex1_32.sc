/*
Exercise 1.32.


a. Show that sum and product (exercise 1.31) are both special cases of a still more general notion called accumulate
that combines a collection of terms, using some general accumulation

function:  (accumulate combiner null-value term a next b)

Accumulate takes as arguments the same term and range specifications as sum and product, together with a combiner
procedure (of two arguments) that specifies how the current term is to be combined with the accumulation of the preceding
terms and a null-value that specifies what base value to use when the terms run out. Write accumulate and show how sum
and product can both be defined as simple calls to accumulate.

b. If your accumulate procedure generates a recursive process, write one that generates an iterative process. If it
  generates an iterative process, write one that generates a recursive process.

*/

//Part A

def accumulate[T,U](combiner: (U,U)=>U, null_value: U, term: T=>U, next: T=>T, a: T, b: T)
                   (implicit numU: Numeric[U], numT: Numeric[T]): U = {
  if (numT.gt(a,b))
    null_value
  else
    combiner(term(a), accumulate(combiner, null_value, term, next, next(a), b))
}

def sum(a:Int, b:Int): Int = accumulate[Int, Int]((x,y)=> x+y, 0, x=>x, x=>x+1, a, b)
sum(1, 4)

def prod(a:Int, b:Int): Int = accumulate[Int, Int]((x,y)=> x*y, 1, x=>x, x=>x+1, a, b)
prod(1,4)

// Part B

def accumulate_iter[T,U](combiner: (U,U)=>U, null_value: U, term: T=>U, next: T=>T, a: T, b: T)
                   (implicit numU: Numeric[U], numT: Numeric[T]): U = {
   @scala.annotation.tailrec
   def acc_helper(acc:U, nextStep:T):U = {
       if (numT.gt(nextStep, b))
           acc
       else
           acc_helper(combiner(term(nextStep), acc), next(nextStep))
   }

   acc_helper(null_value, a)
}

def sum_iter(a:Int, b:Int): Int = accumulate_iter[Int, Int]((x,y)=> x+y, 0, x=>x, x=>x+1, a, b)
sum_iter(1, 4)

def prod_iter(a:Int, b:Int): Int = accumulate_iter[Int, Int]((x,y)=> x*y, 1, x=>x, x=>x+1, a, b)
prod_iter(1,4)

