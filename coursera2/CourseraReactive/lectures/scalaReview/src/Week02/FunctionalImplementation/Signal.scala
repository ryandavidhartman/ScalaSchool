package Week02.FunctionalImplementation


/* Each signal maintains
  * Its current value
  * The current expression that defines the signal value
  * A set of observers: the other signals that depend on its value

  If a signal changes all observers need to be re-evaluated
 */

/* Dependency Maintenance

How do we record dependencies in observers?

  * When evaluating a signal-valued expression, we need to know which
    signal caller gers definded or updated by the expression

  * If we know that, then executing a sig() means adding caller to the
    observers of sig.

  * When a signal sig's value changes, all previous observing signals
    are re-evaluated and then set sig.observers is cleared

  * Re-evaluation will re-enter a calling singal caller in sig.observers,
   as long as caller's value still depends on sig.

 */

/* Who's Calling
  How do we find out on whose behalf a signal expression is evaluated?

  One simple way to do this is maintain a global data structure referring
  to the current caller.  (We will discuss and refine this later)

  That data structure is accessed in stack-like fashion because one
  evalution of a signal might trigger others

 */

class Signal[T](expr : => T) {
  import Signal._
  private var myExpr: () => T = _
  private var myValue: T = _
  private var observers: Set[Signal[_]] = Set()
  update(expr)

  protected def update(expr: => T): Unit = {
    myExpr = () => expr
    computeValue()
  }

  /*
  A signal's current value can change when
    * somebody calls an update operation on a Var, or
    * the value of a dependent signal changes
   */
  protected def computeValue(): Unit = {
    val newValue = caller.withValue(this)(myExpr())

    if(myValue != newValue) {
      myValue = newValue
      val obs = observers
      observers = Set()
      obs.foreach(_.computeValue())
    }

  }

  def apply():T = {
    observers += caller.value
    assert(!caller.value.observers.contains(this), "cyclic signal definition")
    myValue
  }
}

/*  Set Up Object Signal
  We also evaluate signal expressions at the top-level when there is no other
  signa; that's defined or updated

  We use the "sentinel" object NoSignal as the caller for these expressions

 */


object NoSignal extends Signal[Nothing](???) {
  override def computeValue() = ()
}

/*
In a real implemenation we swap out StackableVariables for DynamicVariable, for thread safety
 */


object Signal {
  private val caller = new StackableVariables[Signal[_]](NoSignal) //let's us use StackableVariables of any Signal type
  def apply[T](expr : => T) = new Signal(expr)
}

class Var[T](expr : => T) extends Signal[T](expr){
  override def update(expr : => T): Unit = super.update(expr)
}

object Var {
  def apply[T](expr : => T) = new Var(expr)
}