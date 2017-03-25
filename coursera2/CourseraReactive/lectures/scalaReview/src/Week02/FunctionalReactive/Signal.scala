package Week02.FunctionalReactive

class Signal[T](expr : => T) {
  def apply():T = expr
}

object Signal {
  def apply[T](expr : => T) = new Signal(expr)
}

class Var[T](expr : => T) extends Signal(expr){
  var value: () => T = () => expr
  override def apply() : T = value()

  def update(newVal : => T) = value = () => newVal
}

object Var {
  def apply[T](expr : => T) = new Var(expr)
}