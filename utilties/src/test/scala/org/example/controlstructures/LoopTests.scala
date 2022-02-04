package org.example.controlstructures

import org.scalatest.funsuite.AnyFunSuite
import org.example.controlstructures.Loops.{break, breakable, whilst}

class LoopTests extends AnyFunSuite{

  test("The Test the whilst loop") {

    var i = 0

    whilst(i < 5) {
      println("Hi! " + i)
      i += 1
    }

    assert(i == 5)
  }

  test("The breakable loop") {
    var counter = 0

    breakable {
      for (i <- 1 to 10) {
        println(i)
        counter  +=1
        if (counter > 4) break  // break out of the for loop at 5
      }
    }
    assert(counter == 5)
  }

}
