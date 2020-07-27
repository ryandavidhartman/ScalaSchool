package ScalaScheme

import org.scalatest.wordspec.AnyWordSpec
import ScalaScheme.Primitives._

class PrimitivesSpecs extends AnyWordSpec {

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
        assert(car(cdr(pair5).asInstanceOf[SchemeList]) == 2)
        assert(cadr(pair5) == 2)
        assert(cdr(cdr(pair5).asInstanceOf[SchemeList]) == cons(3, 4))
        assert(cddr(pair5) == cons(3, 4))
      }
    }

  }

  "car and cdr" when {
    "succeed" should {
      "with cons pairs" in {
        val pair1 = cons(1, 2)
        assert(car(pair1) == 1)
        assert(cdr(pair1) == 2)
      }

      "with lists" in {
        val list1 = SchemeList(1, 2)
        assert(car(list1) == 1)
        assert(cdr(list1) == SchemeList(2))
      }

    }
  }

  "isPair" when {
    "succeed" should {
      "with cons pairs" in {
        val pair1 = cons(1, 2)
        assert(isPair(pair1))
      }

      "with lists" in {
        val list1 = SchemeList(1, 2)
        assert(isPair(list1))
      }
    }
    "fail" should {
      "with nil" in {
        assert(!isPair(Nil))
      }
      "with an int" in {
        assert(!isPair(101))
      }
    }

  }

  "isNull" when {
    "false" should {
      "with cons pairs" in {
        val pair1 = cons(1, 2)
        assert(!isNull(pair1))
      }

      "with lists" in {
        val list1 = SchemeList(1, 2)
        assert(!isNull(list1))
      }

      "with an int" in {
        assert(!isNull(101))
      }
    }
    "true" should {
      "with nil" in {
        assert(isNull(Nil))
      }
    }
  }
}
