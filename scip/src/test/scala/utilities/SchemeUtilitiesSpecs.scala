package utilities

import org.scalatest.wordspec.AnyWordSpec
import utilities.SchemeUtilities._

class SchemeUtilitiesSpecs extends AnyWordSpec {

  case class Stuff(a: Int, b: Double)

  "cons" when {
    "combining data" should {
      "succeed with simple data" in {
        val pair1 = cons(1, 2)
        assert(car(pair1) == 1)
        assert(cdr(pair1) == 2)

        val pair2 = cons("1", "2")
        assert(car(pair2) == "1")
        assert(cdr(pair2) == "2")

        val pair3 = cons(1, "2")
        assert(car(pair3) == 1)
        assert(cdr(pair3) == "2")
      }
      "succeed with complex data" in {
        val pair1 = cons(1, 2)
        assert(car(pair1) == 1)
        assert(cdr(pair1) == 2)

        val pair2 = cons("3", "4")
        assert(car(pair2) == "3")
        assert(cdr(pair2) == "4")

        val pair3 = cons(pair1, pair2)
        assert(car(pair3) == pair1)
        assert(cdr(pair3) == pair2)

        val stuff1 = Stuff(1, 2.0)
        val pair4 = cons(stuff1, pair3)
        assert(car(pair4) == stuff1)
        assert(cdr(pair4) == pair3)

        val pair5 = cons(1, cons(2, cons(3, 4)))
        assert(car(pair5) == 1)
        assert(car(cdr(pair5)) == 2)
        assert(cdr(cdr(pair5)) == cons(3, 4))
        assert(car(cdr(cdr(pair5))) == 3)
        assert(cdr(cdr(cdr(pair5))) == 4)

      }
    }

  }

  "car and cdr" when {
    "succeed" should {
      "succeed with cons pairs" in {
        val pair1 = cons(1, 2)
        assert(car(pair1) == 1)
        assert(cdr(pair1) == 2)
      }

      "succeed with lists" in {
        val list1 = List(1, 2)
        assert(car(list1) == 1)
        assert(cdr(list1) == List(2))
      }

    }
  }

  "multiply" when {
    "multiplying valid numbers" should {
      "succeed" in {
        assert(multiply() == 1.0)
        assert(multiply(-1, 3) == -3.0)
        assert(multiply(2, 3, 3.33) == 19.98)
        assert(multiply("223", 2) == 446.0)
      }
    }
    "multiplying invalid numbers" should {
      "failed" in {
        assertThrows[NumberFormatException] {
          multiply("223c")
        }

        assertThrows[NumberFormatException] {
          multiply(Stuff(223, 1), 1, 2.3)
        }
      }
    }
  }

  "division" when {
    "dividing valid numbers" should {
      "succeed" in {
        assert(division(-1, 3) == -1.0/3)
        assert(division(2, 3, 3.33) == 2.0/9.99)
        assert(division("224", 2) == 112.0)
      }
    }
    "dividing invalid numbers" should {
      "failed" in {
        assertThrows[IllegalArgumentException] {
          division()
        }

        assertThrows[NumberFormatException] {
          division(Stuff(223, 1), 1, 2.3)
        }
      }
    }
  }
}
