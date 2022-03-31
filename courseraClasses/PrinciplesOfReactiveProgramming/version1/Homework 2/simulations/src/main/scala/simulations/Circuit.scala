package simulations

import common._

class Wire {
  private var sigVal = false
  private var actions: List[Simulator#Action] = List()

  def getSignal: Boolean = sigVal

  def setSignal(s: Boolean) {
    if (s != sigVal) {
      sigVal = s
      actions.foreach(action => action())
    }
  }

  def addAction(a: Simulator#Action) {
    actions = a :: actions
    a()
  }
}

abstract class CircuitSimulator extends Simulator {

  val InverterDelay: Int
  val AndGateDelay: Int
  val OrGateDelay: Int

  def probe(name: String, wire: Wire) {
    wire addAction {
      () =>
        afterDelay(0) {
          println(
            "  " + currentTime + ": " + name + " -> " + wire.getSignal)
        }
    }
  }

  def inverter(input: Wire, output: Wire) {
    def invertAction() {
      val inputSig = input.getSignal
      afterDelay(InverterDelay) { output.setSignal(!inputSig) }
    }
    input addAction invertAction
  }

  def andGate(a1: Wire, a2: Wire, output: Wire) {
    def andAction() {
      val a1Sig = a1.getSignal
      val a2Sig = a2.getSignal
      afterDelay(AndGateDelay) { output.setSignal(a1Sig & a2Sig) }
    }
    a1 addAction andAction
    a2 addAction andAction
  }

  def andGate(in: Wire, inputs: List[Wire], output: Wire) {
    inputs.length match {
      case 0 => andGate(in, in, output)
      case 1 => andGate(in, inputs.head, output)
      case _ => {
        val out = new Wire
        andGate(inputs.head, in, out)
        andGate(out, inputs.tail, output)
      }
    }
  }

  def orGate(a1: Wire, a2: Wire, output: Wire) {
    def orAction() {
      val a1Sig = a1.getSignal
      val a2Sig = a2.getSignal
      afterDelay(OrGateDelay) { output.setSignal(a1Sig | a2Sig) }
    }
    a1 addAction orAction
    a2 addAction orAction
  }

  def orGate2(a1: Wire, a2: Wire, output: Wire) {
    val b1, b2, c = new Wire

    inverter(a1, b1)
    inverter(a2, b2)
    andGate(b1, b2, c)
    inverter(c, output)
  }

  def demux(in: Wire, c: List[Wire], out: List[Wire]) {

    var invertions = Array.fill(c.length)(true)
    val lastIndex = out.length - 1
    for (index <- lastIndex to 0 by -1) {

      var inputWires = List[Wire]()

      var controlNumber = 0
      val lastContorlNumber = c.length - 1
      for (controlNumber <- lastContorlNumber to 0 by -1) {

        if ((lastIndex - index) % (math.pow(2, controlNumber)) == 0)
          invertions(controlNumber) = !invertions(controlNumber)

        if (invertions(controlNumber))
          inputWires = c(controlNumber) :: inputWires
        else {
          val inverted = new Wire
          inverter(c(controlNumber), inverted)
          inputWires = inverted :: inputWires
        }
      }

      andGate(in, inputWires, out(index))
    }

  }
}

object Circuit extends CircuitSimulator {
  val InverterDelay = 1
  val AndGateDelay = 3
  val OrGateDelay = 5

  def andGateExample {
    val in1, in2, out = new Wire
    andGate(in1, in2, out)
    probe("in1", in1)
    probe("in2", in2)
    probe("out", out)
    in1.setSignal(false)
    in2.setSignal(false)
    run

    in1.setSignal(true)
    run

    in2.setSignal(true)
    run
  }

  //
  // to complete with orGateExample and demuxExample...
  //
}

object CircuitMain extends App {
  // You can write tests either here, or better in the test class CircuitSuite.
  Circuit.andGateExample
}
