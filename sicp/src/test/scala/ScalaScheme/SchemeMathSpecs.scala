package ScalaScheme

import ScalaScheme.Primitives.SD
import SchemeMath._
import org.scalatest.wordspec.AnyWordSpec

class SchemeMathSpecs  extends AnyWordSpec {

  case class Stuff(a: Int, b: Double)

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
        assert(division(-1, 3) == -1.0 / 3)
        assert(division(2, 3, 3.33) == 2.0 / 9.99)
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

  "square" when {
    "when used with numeric data" should {
      "succeed for ints" in {
        val i:SD = 5
        assert(square(i) === 25)
      }

      "succeed for doubles" in {
        val i:SD = -5.0
        assert(square(i) === 25)
      }

      "succeed for numeric strings" in {
        val i:SD = "6"
        assert(square(i) === 36)
      }
    }

    "when used with non-numeric data" should {
      "fail for non-numeric strings" in {
        val i:SD = "6a"
        assertThrows[NumberFormatException](square(i))
      }
    }
  }

  "isDivisor" when {
    "when used with numeric data" should {
      "succeed for ints" in {
        val i1:SD = 10
        val j1:SD = 5
        assert(isDivisor(i1, j1))

        val i2:SD = 11
        val j2:SD = 5
        assert(!isDivisor(i2, j2))
      }

      "succeed for doubles" in {
        val i1:SD = 11.0
        val j1:SD = 5.5
        assert(isDivisor(i1, j1))

        val i2:SD = 12.5
        val j2:SD = 5
        assert(!isDivisor(i2, j2))
      }

      "succeed for numeric strings" in {
        val i1:SD = "11.0"
        val j1:SD = "5.5"
        assert(isDivisor(i1, j1))

        val i2:SD = "12.5"
        val j2:SD = "5"
        assert(!isDivisor(i2, j2))
      }
    }

    "when used with non-numeric data" should {
      "fail for non-numeric strings" in {
        val i:SD = "6a"
        val j:SD = "5"
        assertThrows[NumberFormatException](isDivisor(i,j))
      }
    }
  }

  "smallestDivisor" when {
    "when used with numeric data" should {
      "succeed for ints" in {
        val i1:SD = 10
        assert(smallestDivisor(i1) == 2)

        val i2:SD = 11
        assert(smallestDivisor(i2) == i2)
      }

      "succeed for doubles" in {
        val i1:SD = 11.0
        val j1:SD = 5.5
        assert(isDivisor(i1, j1))

        val i2:SD = 12.5
        val j2:SD = 5
        assert(!isDivisor(i2, j2))
      }

      "succeed for numeric strings" in {
        val i1:SD = "11.0"
        val j1:SD = "5.5"
        assert(isDivisor(i1, j1))

        val i2:SD = "12.5"
        val j2:SD = "5"
        assert(!isDivisor(i2, j2))
      }
    }

    "when used with non-numeric data" should {
      "fail for non-numeric strings" in {
        val i:SD = "6a"
        assertThrows[java.lang.NumberFormatException](smallestDivisor(i))
      }
    }
  }
}
