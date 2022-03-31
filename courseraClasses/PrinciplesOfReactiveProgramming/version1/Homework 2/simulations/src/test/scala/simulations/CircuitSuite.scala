package simulations

import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class CircuitSuite extends CircuitSimulator with FunSuite {
  val InverterDelay = 1
  val AndGateDelay = 3
  val OrGateDelay = 5

  test("andGate example") {
    val in1, in2, out = new Wire
    andGate(in1, in2, out)
    in1.setSignal(false)
    in2.setSignal(false)
    run

    assert(out.getSignal === false, "and 1")

    in1.setSignal(true)
    run

    assert(out.getSignal === false, "and 2")

    in2.setSignal(true)
    run

    assert(out.getSignal === true, "and 3")
  }

  test("orGate") {
    val in1, in2, out = new Wire
    orGate(in1, in2, out)
    in1.setSignal(false)
    in2.setSignal(false)
    run

    assert(out.getSignal === false, "or 1")

    in1.setSignal(true)
    run

    assert(out.getSignal === true, "or 2")

    in2.setSignal(true)
    run

    assert(out.getSignal === true, "or 3")
  }

  test("orGate2") {
    val in1, in2, out = new Wire
    orGate2(in1, in2, out)
    in1.setSignal(false)
    in2.setSignal(false)
    run

    assert(out.getSignal === false, "or 1")

    in1.setSignal(true)
    run

    assert(out.getSignal === true, "or 2")

    in2.setSignal(true)
    run

    assert(out.getSignal === true, "or 3")
  }

  test("demux simplest") {
    val in, y = new Wire
    val c = List()
    val out = List(y)

    in.setSignal(false)

    demux(in, c, out)
    run

    assert(y.getSignal === false, "demux simplest 1")

    in.setSignal(true)

    demux(in, c, out)
    run

    assert(y.getSignal === true, "demux simplest 2")
  }

  test("demux one input") {
    val in, c0, y1, y0 = new Wire
    val c = List(c0)
    val out = List(y1, y0)

    in.setSignal(false)
    c0.setSignal(false)

    demux(in, c, out)
    run

    assert(y0.getSignal === false, "demux one input 1: y0")
    assert(y1.getSignal === false, "demux one input 1: y1")

    c0.setSignal(true)

    demux(in, c, out)
    run

    assert(y0.getSignal === false, "demux one input 2: y0")
    assert(y1.getSignal === false, "demux one input 2: y1")

    in.setSignal(true)
    c0.setSignal(true)

    demux(in, c, out)
    run

    assert(y0.getSignal === false, "demux one input 2: y0")
    assert(y1.getSignal === true, "demux one input 2: y1")

    c0.setSignal(false)

    demux(in, c, out)
    run

    assert(y0.getSignal === true, "demux one input 2: y0")
    assert(y1.getSignal === false, "demux one input 2: y1")
  }

  test("demux two inputs") {
    val in, c0, c1, y0, y1, y2, y3 = new Wire
    val c = List(c1, c0)
    val out = List(y3, y2, y1, y0)

    out foreach (y => y.setSignal(true))

    in.setSignal(false)
    c0.setSignal(true)
    c1.setSignal(true)

    demux(in, c, out)
    run

    out foreach (y =>
      assert(y.getSignal === false, "demux two input, no signal"))

    in.setSignal(true)

    c0.setSignal(false)
    c1.setSignal(false)

    demux(in, c, out)
    run

    assert(y0.getSignal === true, "demux two input 1: y0")
    assert(y1.getSignal === false, "demux two input 1: y1")
    assert(y2.getSignal === false, "demux two input 1: y2")
    assert(y3.getSignal === false, "demux two input 1: y3")

    c0.setSignal(true)
    c1.setSignal(false)

    demux(in, c, out)
    run

    assert(y0.getSignal === false, "demux two input 2: y0")
    assert(y1.getSignal === false, "demux two input 2: y1")
    assert(y2.getSignal === true, "demux two input 2: y2")
    assert(y3.getSignal === false, "demux two input 2: y3")

    c0.setSignal(false)
    c1.setSignal(true)

    demux(in, c, out)
    run

    assert(y0.getSignal === false, "demux two input 3: y0")
    assert(y1.getSignal === true, "demux two input 3: y1")
    assert(y2.getSignal === false, "demux two input 3: y2")
    assert(y3.getSignal === false, "demux two input 3: y3")

    c0.setSignal(true)
    c1.setSignal(true)

    demux(in, c, out)
    run

    assert(y0.getSignal === false, "demux two input 4: y0")
    assert(y1.getSignal === false, "demux two input 4: y1")
    assert(y2.getSignal === false, "demux two input 4: y2")
    assert(y3.getSignal === true, "demux two input 4: y3")
  }

  test("demux five inputs") {

    val in, c4, c3, c2, c1, c0, y0, y1, y2, y3, y4, y5, y6, y7, y8, y9, y10, y11, y12, y13, y14, y15, y16, y17, y18, y19, y20, y21, y22, y23, y24, y25, y26, y27, y28, y29, y30, y31 = new Wire
    val c = List(c4, c3, c2, c1, c0)
    val out = List(y31, y30, y29, y28, y27, y26, y25, y24, y23, y22, y21, y20, y19, y18, y17, y16, y15, y14, y13, y12, y11, y10, y9, y8, y7, y6, y5, y4, y3, y2, y1, y0)

    in.setSignal(false)
    c2.setSignal(true)
    c1.setSignal(true)
    c0.setSignal(true)

    demux(in, c, out)
    run

    out foreach (y =>
      assert(y.getSignal === false, "demux three input, no signal"))

    in.setSignal(true)

    c4.setSignal(true)
    c3.setSignal(false)
    c2.setSignal(true)
    c1.setSignal(false)
    c0.setSignal(true)

    demux(in, c, out)
    run


    for (i <- 31 to 11 by -1) {
      assert(out(i).getSignal === false, "demux three input 1: y" +  i)
    }
    
    assert(y21.getSignal === true, "demux three input 1: y21")

    for (i <- 9 to 0 by -1) {
      assert(out(i).getSignal === false, "demux three input 1: y" + i)
    }
  }

  test("demux three inputs") {

    val in, c2, c1, c0, y0, y1, y2, y3, y4, y5, y6, y7 = new Wire
    val c = List(c2, c1, c0)
    val out = List(y7, y6, y5, y4, y3, y2, y1, y0)

    in.setSignal(false)
    c2.setSignal(true)
    c1.setSignal(true)
    c0.setSignal(true)

    demux(in, c, out)
    run

    out foreach (y =>
      assert(y.getSignal === false, "demux three input, no signal"))

    in.setSignal(true)

    c2.setSignal(false)
    c1.setSignal(false)
    c0.setSignal(false)

    demux(in, c, out)
    run

    assert(y0.getSignal === true, "demux three input 1: y0")
    assert(y1.getSignal === false, "demux three input 1: y1")
    assert(y2.getSignal === false, "demux three input 1: y2")
    assert(y3.getSignal === false, "demux three input 1: y3")
    assert(y4.getSignal === false, "demux three input 1: y4")
    assert(y5.getSignal === false, "demux three input 1: y5")
    assert(y6.getSignal === false, "demux three input 1: y6")
    assert(y7.getSignal === false, "demux three input 1: y7")

    c2.setSignal(true)
    c1.setSignal(false)
    c0.setSignal(false)

    demux(in, c, out)
    run

    assert(y0.getSignal === false, "demux three input 2: y0")
    assert(y1.getSignal === true, "demux three input 2: y1")
    assert(y2.getSignal === false, "demux three input 2: y2")
    assert(y3.getSignal === false, "demux three input 2: y3")
    assert(y4.getSignal === false, "demux three input 2: y4")
    assert(y5.getSignal === false, "demux three input 2: y5")
    assert(y6.getSignal === false, "demux three input 2: y6")
    assert(y7.getSignal === false, "demux three input 2: y7")

    c2.setSignal(true)
    c1.setSignal(true)
    c0.setSignal(false)

    demux(in, c, out)
    run

    assert(y0.getSignal === false, "demux three input 3: y0")
    assert(y1.getSignal === false, "demux three input 3: y1")
    assert(y2.getSignal === false, "demux three input 3: y2")
    assert(y3.getSignal === true, "demux three input 3: y3")
    assert(y4.getSignal === false, "demux three input 3: y4")
    assert(y5.getSignal === false, "demux three input 3: y5")
    assert(y6.getSignal === false, "demux three input 3: y6")
    assert(y7.getSignal === false, "demux three input 3: y7")

    c2.setSignal(false)
    c1.setSignal(true)
    c0.setSignal(true)

    demux(in, c, out)
    run

    assert(y0.getSignal === false, "demux three input 4: y0")
    assert(y1.getSignal === false, "demux three input 4: y1")
    assert(y2.getSignal === false, "demux three input 4: y2")
    assert(y3.getSignal === false, "demux three input 4: y3")
    assert(y4.getSignal === false, "demux three input 4: y4")
    assert(y5.getSignal === false, "demux three input 4: y5")
    assert(y6.getSignal === true, "demux three input 4: y6")
    assert(y7.getSignal === false, "demux three input 4: y7")

    c2.setSignal(true)
    c1.setSignal(true)
    c0.setSignal(true)

    demux(in, c, out)
    run

    assert(y0.getSignal === false, "demux three input 5: y0")
    assert(y1.getSignal === false, "demux three input 5: y1")
    assert(y2.getSignal === false, "demux three input 5: y2")
    assert(y3.getSignal === false, "demux three input 5: y3")
    assert(y4.getSignal === false, "demux three input 5: y4")
    assert(y5.getSignal === false, "demux three input 5: y5")
    assert(y6.getSignal === false, "demux three input 5: y6")
    assert(y7.getSignal === true, "demux three input 5: y7")
  }
}
