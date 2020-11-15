import scala.annotation.tailrec

//
// Problem 1
// Crash you program with an out of memory error.
//
@tailrec
def HeapExploder(l: List[Int]): List[Int] = {
  HeapExploder(l ++ l ++ l ++ l ++ l)
}

//val problem1 = HeapExploder(List(1,2,3,4,5,6))
// java.lang.OutOfMemoryError: Java heap space


//
// Problem 2
// Crash you program with a stack over flow
//

def factorial(n: BigInt): BigInt = {
  if(n == 1)
    1
  else
    n * factorial(n-1)
}

//val problem2 = factorial(1000000)
//java.lang.StackOverflowError

//
// Problem 3
//

// Make a PocketCalculator
//  - add(x,y)
//  - subtract(x,y)
//  - multiply(x,y)
//  - divide(x,y)

// Throw
//  - OverflowException if an operation exceeds Int.MAX_VALUE
//  - UnderflowException if operation is less than Int.MIN_VALUE
//  - DivideByZeroException for division by 0

class OverflowException extends Exception
class UnderflowException extends Exception
class DivideByZeroException extends Exception

object PocketCalculator {
  def add(x:  Int, y: Int): Int = {
    val result = x + y
    if(x > 0 && y > 0 && result < 0) throw new OverflowException
    if(x < 0 && y < 0 && result > 0) throw new UnderflowException
    result
  }

  def subtract(x: Int, y: Int): Int = add(x, -y)

  def multiple(x:  Int, y: Int): Int = {
    val result = x * y
    if(x > 0 && y > 0 && result < 0) throw new OverflowException
    if(x < 0 && y < 0 && result < 0) throw new OverflowException
    if(x > 0 && y < 0 && result > 0) throw new UnderflowException
    if(x > 0 && y < 0 && result > 0) throw new UnderflowException
    result
  }

  def divide(x: Int, y: Int): Double = {
    if(y == 0) throw new DivideByZeroException
    x/(y*1.0)
  }
}

//PocketCalculator.add(Int.MaxValue,  1)
//PocketCalculator.add(Int.MinValue,  -10)
//PocketCalculator.subtract(Int.MinValue,  10)
//PocketCalculator.subtract(Int.MaxValue,  -10)