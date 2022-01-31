package org.example.utilities

import org.scalatest.funsuite.AnyFunSuite

class TimerTests extends AnyFunSuite {

  test("The timer returns a time greater than 0") {
    val (_, time) = Timer.time({
      println("Hi Guy!")
    })
    assert(time > 0)
  }

  test("The timer returns sane results") {
    val sleepTime = 100L
    val (_, time) = Timer.time(Thread.sleep(sleepTime))
    assert(time > sleepTime)
    assert(time < sleepTime + 3)
  }

}
