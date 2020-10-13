import org.scalatest.flatspec.AnyFlatSpec

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
}
