package quickcheck

import common._

import org.scalacheck._
import Arbitrary._
import Gen._
import Prop._

abstract class QuickCheckHeap extends Properties("Heap") with IntHeap {

	property("min") = forAll { a: Int =>
		var h1 = insert(2, empty)
		h1 = insert(1, h1)
		h1 = insert(4, h1)
		findMin(h1) == 1
	}
	  
	property("delete") = forAll { a: Int =>
		var h1 = insert(2, empty)
		h1 = insert(1, h1)
		h1 = insert(4, h1)
		h1 = deleteMin(h1)
		findMin(h1) == 2
	}
	  
	property("meld") = forAll { a: Int =>
		var h1 = insert(4, empty)
		h1= insert(2, h1)
		var h2 = insert(3, empty)
		h2 = insert(1, h2)
		val result = meld (h1, h2)
		findMin(result) == 1
	}

  lazy val genHeap: Gen[H] = ???

  implicit lazy val arbHeap: Arbitrary[H] = Arbitrary(genHeap)

}
