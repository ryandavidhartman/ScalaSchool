package Week02.DiscreteEvenSimulation

/*
   It's convenient to pack all delay constants into their own trait which can be
   mixed into a simulation
 */

trait Parameters {
  def InverterDelay = 2
  def AndGateDelay = 3
  def OrGateDelay = 5
}
