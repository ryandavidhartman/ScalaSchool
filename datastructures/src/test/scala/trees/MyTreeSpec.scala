package trees

import example.trees.{Leaf, MyTree, Node}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class MyTreeSpec  extends AnyFlatSpec with Matchers {
  "MyTree" should "construct new Trees" in {
    val testTree = MyTree(1,2,3)
    val expectedTree = Node(1, Leaf, Node(2, Leaf, Node(3, Leaf, Leaf)))

    assert(testTree == expectedTree)
  }

  "MyTree" should "be able to find elements" in {
    val testTree1 = Node(1, Leaf, Leaf)
    assert(testTree1.contains(1))
    assert(!testTree1.contains(3))

    val testTree2 = MyTree(1,2,3)
    assert(testTree2.contains(2))
    assert(!testTree2.contains(4))
  }

  "MyTree" should "be able to map over elements" in {
    val testTree = Node(4, Node(3, Leaf, Leaf), Node(5, Leaf, Leaf))
    val expectedTree1 = Node(-4, Node(-5, Leaf, Leaf), Node(-3, Leaf, Leaf))

    assert(testTree.map(x => -1*x).contains(-4) )
    // assert(testTree.map(x => -1*x) == expectedTree1)
  }
}
