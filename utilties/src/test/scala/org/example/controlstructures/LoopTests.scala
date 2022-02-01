package org.example.controlstructures

import org.scalatest.funsuite.AnyFunSuite
import org.example.controlstructures.Loops.whilst

class LoopTests extends AnyFunSuite{

  test("The Test the whilst loop") {

    var i = 0

    whilst(i < 5) {
      println("Hi! " + i)
      i += 1
    }

    assert(i == 5)
  }

}
