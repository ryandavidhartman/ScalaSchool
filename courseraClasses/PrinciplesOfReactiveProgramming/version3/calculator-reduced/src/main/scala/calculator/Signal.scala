package calculator

import scala.util.DynamicVariable

class Signal[T](expr: => T) {
  import Signal._
  private val counterPos = Signal.getNextCounter()
  println(s"Created signal: $counterPos")
  private var myExpr: () => T = _
  private var myValue: T = _
  private var observers: Set[Signal[_]] = Set()
  private var observed: List[Signal[_]] = Nil
  update(expr)

  def printThread(location:String) = {
    val tid = Thread.currentThread().getId
    println(s"At location $location the TID=$tid")
  }

  protected def computeValue(): Unit = {
    printThread("computeValue")
    println(s"Called computeValue for signal: $counterPos")
    for (sig <- observed)
      sig.observers -= this
    observed = Nil
    val newValue = caller.withValue(this)(myExpr())
    /* Disable the following "optimization" for the assignment, because we
     * want to be able to track the actual dependency graph in the tests.
     */
    //if (myValue != newValue) {
      myValue = newValue
      val obs = observers
      observers = Set()
      obs.foreach(_.computeValue())
    //}
  }

  protected def update(expr: => T): Unit = {
    myExpr = () => expr
    computeValue()
  }

  def apply() = {
    printThread("apply")
    println(s"apply called from signal: $counterPos")
    val callerThreadCounter = caller.value.counterPos
    val thisThreadCounter = this.counterPos
    observers += caller.value
    println(s"apply(): Caller Thread: $callerThreadCounter, Callee(this) Thread: $thisThreadCounter")
    assert(!caller.value.observers.contains(this), "cyclic signal definition")
    caller.value.observed ::= this
    println(s"added this signal to observed")
    myValue
  }
}

class Var[T](expr: => T) extends Signal[T](expr) {
  override def update(expr: => T): Unit = super.update(expr)
}

object Var {
  def apply[T](expr: => T) = new Var(expr)
}

object NoSignal extends Signal[Nothing](???) {
  override def computeValue() = ()
}

object Signal {
  var counter = 0
  def getNextCounter() = {
    counter = counter + 1
    counter
  }
  val caller = new DynamicVariable[Signal[_]](NoSignal)
  def apply[T](expr: => T) = new Signal(expr)
}
