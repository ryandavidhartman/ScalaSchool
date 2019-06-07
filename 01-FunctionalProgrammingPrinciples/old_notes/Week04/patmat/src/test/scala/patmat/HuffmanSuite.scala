package patmat

import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

import patmat.Huffman._

@RunWith(classOf[JUnitRunner])
class HuffmanSuite extends FunSuite {
  trait TestTrees {
    val t1 = Fork(Leaf('a',2), Leaf('b',3), List('a','b'), 5)
    val t2 = Fork(Fork(Leaf('a',2), Leaf('b',3), List('a','b'), 5), Leaf('d',4), List('a','b','d'), 9)
  }

  test("weight of a larger tree") {
    new TestTrees {
      assert(weight(t1) === 5)
    }
  }

  test("chars of a larger tree") {
    new TestTrees {
      assert(chars(t2) === List('a','b','d'))
    }
  }

  test("string2chars(\"hello, world\")") {
    assert(string2Chars("hello, world") === List('h', 'e', 'l', 'l', 'o', ',', ' ', 'w', 'o', 'r', 'l', 'd'))
  }

  test("makeOrderedLeafList for some frequency table") {
    assert(makeOrderedLeafList(List(('t', 2), ('e', 1), ('x', 3))) === List(Leaf('e',1), Leaf('t',2), Leaf('x',3)))
  }

  test("combine of some leaf list") {
    val leaflist = List(Leaf('e', 1), Leaf('t', 2), Leaf('x', 4))
    assert(combine(leaflist) === List(Fork(Leaf('e',1),Leaf('t',2),List('e', 't'),3), Leaf('x',4)))
  }

  test("create code tree") {
    val codeTree = createCodeTree(string2Chars("sometext"))

  }

  test("decode and encode a very short text should be identity") {
    new TestTrees {
      assert(decode(t1, encode(t1)("ab".toList)) === "ab".toList)
    }
  }

  test("times should calculate the frequency of a character in the text") {
    val list1 = List('a', 'b', 'a', 'a')
    assert(3 === list1.count(p => p == 'a'))
    val check1 = times(list1)
    assert(check1.head._1 === 'a')
    assert(check1.head._2 === 3)
    assert(check1.tail.head._1 === 'b')
    assert(check1.tail.head._2 === 1)
  }

  test("makeOrderedLeafList should Returns a list of `Leaf` nodes for a given frequency table `freqs`") {
    val list1 = List(('a',14), ('b',5), ('c', 22))
    val result1 = makeOrderedLeafList(list1)
    assert(result1.length === 3)
    assert(result1.head.char=== 'b')
    assert(result1.head.weight === 5)
    assert(result1.tail.head.char === 'a')
    assert(result1.tail.head.weight === 14)
  }

  test("singleton can determine singleton lists") {
    assert(!singleton(Nil))
    assert(singleton(List(Leaf('a', 5))))
    assert(singleton(List(Fork(Leaf('a', 5), Leaf('b', 6), List('a', 'b'), 11))))
    assert(!singleton(List(Leaf('a', 5), Leaf('b', 6))))
  }

  test("until can combine leaves into a singleton tree")
  {
    val leaves = List(Leaf('a', 5), Leaf('b', 6), Leaf('c', 2))
    val testTree = until(singleton,combine)(leaves)
    assert(!testTree.isEmpty)
    assert(testTree.head.asInstanceOf[Fork].chars === List('a','b','c'))
    assert(testTree.head.asInstanceOf[Fork].weight === 13)
  }

  test("can create huffman code tree")
  {
    val chars = "baababacacbbb"
    val huffmanTree = createCodeTree(chars.toList)
    assert(huffmanTree.asInstanceOf[Fork].chars === List('c','a','b'))
    assert(huffmanTree.asInstanceOf[Fork].weight === 13)
  }

  test("can create code tree optimal length")
  {
    val testData = string2Chars("someText")
    val timesTest = times(testData)
    assert(7 === timesTest.length)

    val leaves = makeOrderedLeafList(timesTest)
    assert(7 === leaves.length)
    assert(1 === leaves.head.weight)
    assert(2 === leaves.last.weight)
  }

  test("Encoding")
  {
    assert(secret === encode(frenchCode)(List('h', 'u', 'f', 'f', 'm', 'a', 'n', 'e', 's', 't', 'c', 'o', 'o', 'l')))
  }

  test("French Code")
  {
    assert(List('h', 'u', 'f', 'f', 'm', 'a', 'n', 'e', 's', 't', 'c', 'o', 'o', 'l') === decodedSecret)
  }

  test("Code Table")
  {
    val fakeTable = List(('a', List(0,1,1)), ('h', List(0,0,1)), ('z', List(1,1,1)))
    assert(List(0,0,1) === codeBits(fakeTable)('h'))
  }

}
