package trees

import example.trees.{Leaf, MyTree, Node}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class MyTreeSpec  extends AnyFlatSpec with Matchers {
  "MyTree trait" should "construct new Trees" in {
    val testTree = MyTree(1,2,3)
    val expectedTree = Node(1, Leaf, Node(2, Leaf, Node(3, Leaf, Leaf)))

    assert(testTree == expectedTree)
  }
}
