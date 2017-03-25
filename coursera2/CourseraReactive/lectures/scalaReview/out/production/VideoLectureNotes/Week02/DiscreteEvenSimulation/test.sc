import Week02.DiscreteEvenSimulation.{Circuits, Parameters}
object test {
  object sim extends Circuits with Parameters
  import sim._
  val in1, in2, sum, carry = new Wire
  halfAdder(in1, in2, sum, carry)
  probe("sum", sum)
  probe("carry", carry)
  in1 setSignal true
  run()
  in2 setSignal true
  run()
  in1 setSignal false
  run()
}