package Week02.FunctionalImplementation

/*
  You access it like this

  val caller = new StackableVar(initialSig)
  caller.withValue(otherSig) { op }
  ... caller.value ...
 */


class StackableVariables[T](init: T) {
  private var values: List[T] = List(init)
  def value: T = values.head
  def withValue[R](newValue: T)(op: => R): R = {
    values = newValue :: values
    try op finally values = values.tail
  }

}
