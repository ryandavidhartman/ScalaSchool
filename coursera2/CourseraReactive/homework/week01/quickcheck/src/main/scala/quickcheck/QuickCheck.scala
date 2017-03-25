package quickcheck

import common._

import org.scalacheck._
import Arbitrary._
import Gen._
import Prop._

abstract class QuickCheckHeap extends Properties("Heap") with IntHeap {

  property("min1") = forAll { a: Int =>
    val h = insert(a, empty)
    findMin(h) == a
  }

  lazy val genHeap: Gen[H] = for {
    n <- arbitrary[A]
    myHeap <- oneOf(const(empty), genHeap)
  } yield insert(n, myHeap)

  implicit lazy val arbHeap: Arbitrary[H] = Arbitrary(genHeap)



  property("min2") = forAll { a: A =>
    val h = insert(a, empty)
    findMin(h) == a
  }

  property("min3") = forAll { (a1: A, a2: A) =>
    val h1 = insert(a1, empty)
    val h2 = insert(a2, h1)
    findMin(h2) == Math.min(a1,a2)
  }

  property("min4") = forAll{ (h1:H, h2:H) =>
    {
      val min1 = findMin(h1)
      val min2 = findMin(h2)
      findMin(meld(h1,h2)) == Math.min(min1,min2)
    }
  }

  property("delete_min1") = forAll { a: A =>
    val h = insert(a, empty)
    val h2 = deleteMin(h)
    isEmpty(h2)
  }

  property("delete_min2") = forAll { h:H => {
    val listOfMin = minList(h)
    listOfMin == listOfMin.sorted
    }
  }

  def minList(h:H):List[Int] = h match {
    case x if(isEmpty(x)) => Nil
    case _ => findMin(h) :: minList(deleteMin(h))
  }

  property("meld") = forAll { (h1: H, h2: H) =>
    val meld1 = meld(h1,h2)
    val meld2 = meld(deleteMin(h1), insert(findMin(h1), h2))
    heapEqual(meld1,meld2)
  }

  def heapEqual(h1: H, h2: H): Boolean =
    if (isEmpty(h1) && isEmpty(h2)) true
    else if (isEmpty(h1) || isEmpty(h2)) false
    else {
      val min1 = findMin(h1)
      val min2 = findMin(h2)
      min1 == min2 && heapEqual(deleteMin(h1), deleteMin(h2))
    }
}
