import Week02.FunctionalReactive.{Signal, BankAccount}
/*
  What is Functional Reactive Programming
  Reactive Programming is about reacting to sequences of events
  that happen in time.
  Functional View: Aggregate an event sequence into a signal
    * A signal is a value that changes over time
    * It is represented as a function from time to value domain
    *
    * Instead of propagating updates to the mutable state, we
     define new signales in terms of existing ones
 */
object accounts  {
  def consolidated(accounts: List[BankAccount]): Signal[Int] =
    Signal(accounts.map(_.balance()).sum)
  val a = new BankAccount()
  val b = new BankAccount()
  val c = consolidated(List(a,b))
  c()
  a deposit 20
  c()

  b deposit 30
  c()

  val xchange = Signal(246.00)
  val inDollar = Signal(c() * xchange())
  inDollar()

  b withdraw 10
  inDollar()
}