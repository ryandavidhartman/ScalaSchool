import scala.annotation.tailrec
// Problem 1: Concatenate a string n times

def concat(s: String, n: Int): String = {

  @tailrec
  def helper(acc: String, i: Int): String =
    if(i <= 0)
      acc
    else
      helper(s + acc, i-1)

  helper("", n)
}

println(s"${concat("scala_", 4)}")


// Problem 2: IsPrime function tail recursive

def isPrime(n: Int): Boolean = {

  @tailrec
  def helper(i:Int): Boolean = {
    if(i <= 1)
      true
    else if(n % i == 0)
      false
    else
      helper(i-1)
  }

  helper(n/2)
}

println(s"Is 100 Prime? ${isPrime(100)}")
println(s"Is 101 Prime? ${isPrime(101)}")


// Problem 3: Fibonacci tail recursive

def fib(n: Int): Int = {

  @tailrec
  def helper(i: Int, acc1: Int, acc2: Int): Int =
    if(i < 1)
      acc1
    else
      helper(i-1, acc2, acc1+acc2)

  helper(n, 1, 1)
}

fib(0)
fib(1)
fib(2)
fib(3)
fib(4)
fib(5)
fib(6)
fib(7)
