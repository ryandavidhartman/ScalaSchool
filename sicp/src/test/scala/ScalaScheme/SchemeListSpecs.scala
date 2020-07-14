package ScalaScheme

import org.scalatest.wordspec.AnyWordSpec
import ScalaScheme.Primitives._

class SchemeListSpecs extends AnyWordSpec {

  "list construction" when {
    "basic list construction" should {
      "succeed for numbers" in {
        val list = SchemeList(1, 2, 3, 4)
        assert(list.head == 1)
        assert(car(list) == 1)
        assert(list.tailSchemeList == cdrL(list))
      }
    }

    "cons list construction" should {
      "succeed for numbers" in {
          val list1a = SchemeList(1)
          val list1b = cons(1, SchemeNil)
          assert(list1a === list1b)

        val list2a = SchemeList(1, 2)
        val list2b = cons(1, cons(2,SchemeNil))
        assert(list2a === list2b)
      }
    }
  }

  "foldLeft" when {
    "combining numeric data" should {
      "succeed for summation" in {
        val list1 = SchemeList(1,2,3,4,5)
        val add: (SD, SD) => SD = (zero, rest) => sum(zero, rest)
        val mySum = list1.foldLeft(0)(add)
        assert(mySum === 15)
      }

      "succeed for multiplication" in {
        val list1 = SchemeList(1,2,3,4,5)
        val product: (SD, SD) => SD = (zero, rest) => multiply(zero, rest)
        val myProduct = list1.foldLeft(1)(product)
        assert(myProduct === 120)
      }

      "succeed for division" in {
        val list1 = SchemeList(1,2,3)
        val divider: (SD, SD) => SD = (zero, rest) => division(zero, rest)
        val myProduct = list1.foldLeft(1)(divider)
        assert(myProduct === 1.0/6)
      }
    }

  }

  "foldRight" when {
    "combining numeric data" should {
      "succeed for summation" in {
        val list1 = SchemeList(1,2,3,4,5)
        //val add: (Any, Any) => Any = (l, r) => l.asInstanceOf[Int] + r.asInstanceOf[Int]
        val add: (SD, SD) => SD = (zero, rest) => sum(zero, rest)
        val mySum = list1.foldRight(0)(add)
        assert(mySum === 15)
      }

      "succeed for multiplication" in {
        val list1 = SchemeList(1,2,3,4,5)
        val product: (SD, SD) => SD = (zero, rest) => multiply(zero, rest)
        val myProduct = list1.foldRight(1)(product)
        assert(myProduct === 120)
      }

      "succeed for division" in {
        val list1 = SchemeList(1,2,3)
        val divider: (SD, SD) => SD = (zero, rest) => division(zero, rest)
        val myProduct = list1.foldRight(1)(divider)
        assert(myProduct === 1.5)
      }
    }

  }

  "length" when {
    "should find the correct length" in {
      val list1 = SchemeNil
      assert(list1.length == 0)

      val list2 = SchemeList(1, 2, 3, 4, 5, 6)
      assert(list2.length == 6)

      val list3 = SchemeList(list1, list2)
      assert(list3.length == 2)
    }
  }
}
