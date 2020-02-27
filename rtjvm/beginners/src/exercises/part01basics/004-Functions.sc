import scala.annotation.tailrec
// A greeting function

def greeting(name: String, age: Int): Unit = println(s"Hi, my name is $name and I'm $age years old")

greeting("Ryan", 22)

// A recursive string concat
def cat(s:String, n: Int): String = {

  @scala.annotation.tailrec
  def helper(n: Int, acc:String): String =
    if(n <= 0) acc
    else helper(n-1, s+acc)

  helper(n,"")
}

println(cat("bob", 5))


// A recursive factorial function
def fact(n: Int): Int = {
  @tailrec
  def helper(n:Int, acc: Int):Int = n match {
    case 1 => acc
    case j => helper(j-1, acc*j)
  }

  helper(n, 1)
}

fact(5)

// A Fibonacci Function
//f(1) = 1, f(2) = 1, f(n) = f(n-1) + f(n-2)
def fib(n: Int): Int = {
  @tailrec
  def helper(n:Int, acc1: Int, acc2: Int):Int = n match {
    case 1 => acc1
    case j => helper(j-1, acc2, acc1 + acc2)
  }

  helper(n, 1, 1)
}

fib(1)
fib(2)
fib(3)
fib(4)
fib(5)

//Test if a number is prime
def divides(a:Int, b:Int): Boolean = b % a == 0

@scala.annotation.tailrec
def findDivisor(n: Int, test:Int): Int = {
  if(test*test > n) n
  else if(divides(test, n)) test
  else findDivisor(n, test+1)
}

def smallestDivisor(n:Int):Int = findDivisor(n,2)
def isPrime(n:Int):Boolean = n == smallestDivisor(n)

isPrime(5)
isPrime(6)


