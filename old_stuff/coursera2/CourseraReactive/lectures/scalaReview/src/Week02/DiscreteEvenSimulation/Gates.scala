package Week02.DiscreteEvenSimulation

abstract class Gates extends Simulation {

  def InverterDelay: Int
  def AndGateDelay: Int
  def OrGateDelay: Int

  /*
  The state of the wire is modeled by two private variables
  sigVal represents the current value of the signal.
  actions represents the actions currently attached to the wire
   */

  class Wire {

    private var sigVal = false
    private var actions: List[Action] = List()

    def getSignal = sigVal

    def setSignal(s: Boolean) =
      if (s != sigVal) {
        sigVal = s
        actions foreach (_()) // == for(a <- actions) a()
      }

    def addAction(a: Action) = {
      actions = a :: actions
      a()
    }
  }

  /*  The Inverter
    We implement the inverter by installing an action on its input wire.  This action
    produces the inverse of the input signal on the output wire.

    The change must be effective after a delay of InverterDelay units of simulated time

   */

  def inverter(input: Wire, output: Wire): Unit = {
    def invertAction(): Unit = {
      val inputSig = input.getSignal
      afterDelay(InverterDelay) { output setSignal !inputSig }
    }
    input addAction invertAction
  }

  /*  The AND Gate
  The AND gate is implemented in a similiar way

  The action of an AND age produces the conjuction of input signals on the output wire.

  Thsi happens after a delay of AndGateDelay unit of similated time.

   */

  def andGate(in1: Wire, in2: Wire, output: Wire) = {
    def andAction() = {
      val in1Sig = in1.getSignal
      val in2Sig = in2.getSignal
      afterDelay(AndGateDelay) {
        output setSignal (in1Sig & in2Sig)
      }
    }
    in1 addAction andAction
    in2 addAction andAction
  }

  def orGateALT(in1: Wire, in2: Wire, output: Wire): Unit = {
    def orAction() = {
      val in1Sig = in1.getSignal
      val in2Sig = in2.getSignal
      afterDelay(OrGateDelay) {
        output setSignal (in1Sig | in2Sig)
      }
    }
    in1 addAction orAction
    in2 addAction orAction
  }
  

  /*
  Before launchign the simulation, we still need a way to examine the changes
  of the signals on the wires.
   */
  def probe(name: String, wire: Wire): Unit = {
    def probeAction(): Unit = {
      println(s"$name $currentTime value = ${wire.getSignal}")
    }
    wire addAction probeAction
  }
  
  /** Design orGate in terms of andGate, inverter */

  def orGate(in1: Wire, in2: Wire, output: Wire): Unit = {
    val notIn1, notIn2, notOut = new Wire
    inverter(in1, notIn1)
    inverter(in2, notIn2)
    andGate(notIn1, notIn2, notOut)
    inverter(notOut, output)
  }
}
