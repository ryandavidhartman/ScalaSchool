package quickcheck

import org.scalacheck.*
import Arbitrary.*
import Gen.*
import Prop.*

class HeapProperties(heapInterface: HeapInterface) extends Properties("Heap"):
  
  // Import all the operations of the `HeapInterface` (e.g., `empty`
  // `insert`, etc.)
  import heapInterface.*


  // Examples of properties
  property("inserting the minimal element and then finding it should return the same minimal element") =
    forAll { (heap: List[Node]) =>
      val min = if isEmpty(heap) then 0 else findMin(heap)
      findMin(insert(min, heap)) == min
    }

  property("the minimum of a heap of two elements should be the smallest of the two elements") =
    forAll { (x1: Int, x2: Int) =>
      val heap = insert(x2, insert(x1, empty))
      val min: Int = math.min(x1, x2)
      findMin(heap) == min
    }

  property("delete minumum of heap of one element should return an empty heap") =
    forAll { (x: Int) =>
      // create a heap with exactly one element, `x`
      val heap1: List[Node] = insert(x, empty)
      // delete the minimal element from it
      val heap0: List[Node] = deleteMin(heap1)
      // check that heap0 is empty
      heap0 == List.empty[Node]

    }

  property("continually finding and deleting the minimal element of a heap should return a sorted sequence") =
    // recursively traverse the heap
    def check(heap: List[Node]): Boolean =
      // if the heap is empty, or if it has just one element, we have
      // successfully finished our checks
      if isEmpty(heap) || isEmpty(deleteMin(heap)) then
        true
      else
        // find the minimal element
        val x1: Int = findMin(heap)
        // delete the minimal element of `heap`
        val heap2: List[Node] = deleteMin(heap)
        // find the minimal element in `heap2`
        val x2: Int = findMin(heap2)
        // check that the deleted element is smaller than the minimal element
        // of the remaining heap, and that the remaining heap verifies the
        // same property (by recursively calling `check`)
        val checked: Boolean = if x1 <= x2 then
          check(heap2)
        else
          false
        checked
    // check arbitrary heaps
    forAll { (heap: List[Node]) =>
      check(heap)
    }

  // TODO Write more properties here to detect the bugs
  // in bogus BinomialHeap implementations

  property("Given an empty heap and a sequence of integers, insert all the "
    + "integers into the heap. Then, repeatedly finding and deleting the minimum "
    + "should give you back a sorted version of the initial sequence of integers") =
    def check2(list: List[Int]): Boolean =
      def buildHeap(heap: List[Node], data: List[Int]): List[Node] =
        if data.isEmpty then
          heap
        else
          buildHeap(insert(data.head, heap), data.tail)

      def tearDownHeap(acc: List[Int], data: List[Node]): List[Int] =
        if data.isEmpty then
          acc
        else
          tearDownHeap(findMin(data) +: acc, deleteMin(data))

      val testHeap = buildHeap(List.empty[Node], list)
      val testlist = tearDownHeap(List.empty[Int], testHeap).reverse

      testlist == list.sorted

    forAll { (list: List[Int]) =>
      check2(list)
    }

  property("Given two arbitrary heaps, and the heap that results from melding " +
           "them together, finding the minimum of the melded heap should return " +
           "the minimum of one or the other of the initial heaps. Then, continuously " +
           "deleting that minimum element (from both, the melded heap and the initial " +
           "heap that contained it), should always give back a melded heap whose minimum " +
           "element is the minimum element of none or the other of the initial heaps, " +
           "until the melded heap is empty.") =
    def check3(left: List[Node], right: List[Node]): Boolean =

      def safeCheck(min: Int, h: List[Node]): Boolean =
        if(h.isEmpty) then
          false
        else
          min == findMin(h)

      def safeDelete(found: Boolean, h: List[Node]): List[Node] =
        if(found && h.nonEmpty) then
          deleteMin(h)
        else
          h

      def checkHeaps(l: List[Node], r: List[Node], m: List[Node]): Boolean =
        if(m.isEmpty) then
          true
        else
          val min = findMin(m)
          val foundLeft = safeCheck(min, l)
          val foundRight = safeCheck(min, r)
          if(foundLeft || foundRight)
            checkHeaps(safeDelete(foundLeft, l), safeDelete(foundRight && !foundLeft, r), deleteMin(m))
          else
            false

      if(left.isEmpty && right.isEmpty) then
        true
      else
        checkHeaps(left, right, meld(left, right))


    forAll { (heap1: List[Node], heap2: List[Node]) =>
      check3(heap1, heap2)
    }

  // random heap generator --- DO NOT MODIFY
  private lazy val genHeap: Gen[List[Node]] = oneOf(const(empty),
    for
      v <- arbitrary[Int]
      h <- oneOf(const(empty), genHeap)
    yield insert(v, h)
  )

  private given Arbitrary[List[Node]] = Arbitrary(genHeap)
  
end HeapProperties
