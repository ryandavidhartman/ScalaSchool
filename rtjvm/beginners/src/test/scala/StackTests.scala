import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.{convertToAnyShouldWrapper, equal}
import org.scalatest.prop.TableDrivenPropertyChecks.{Table, forAll}
import org.scalatest.prop.TableFor1

import scala.collection.mutable


class StackTests extends AnyFlatSpec {

  "A Stack" should "pop values in last-in-first-out order" in {
    val stack = new mutable.Stack[Int]
    stack.push(0)
    stack.push(1)
    assert(stack.pop() === 1)
    assert(stack.pop() === 0)
  }

  it should "throw NoSuchElementException if an empty stack is popped" in {
    assertThrows[NoSuchElementException] {
      val emptyStack = new mutable.Stack[Int]
      emptyStack.pop()
    }
  }

  val first14FiboNums: TableFor1[Int] =  Table("n", 0, 1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89, 144, 233)

  def fib(n: Int): Int = {
    if(n <= 0)
      0
    else if(n <= 2)
      1
    else
     fib(n-2) + fib(n-1)
  }


  "Fibonacci Test" should "work" in {
    var i = 0
    forAll(first14FiboNums) { n =>
      fib(i) should equal(n)
      i = i+1
    }
  }
}
