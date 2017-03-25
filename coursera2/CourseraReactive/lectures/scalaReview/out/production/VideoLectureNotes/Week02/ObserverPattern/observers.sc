import Week02.ObserverPattern.{Consolidator,BankAccount}

/*
Observer Pattern
Good:
  Decouples views from state
  Supports multiple views for a given state (model)
  Simple to set up

Bad:
  Forces imperative style, since handlers are Unit-typed
  Many moving parts to coordinate
  Concurrency makes thigns more complicated
  Views are tightly bound to one state, view update happens
  immediately
 */



object observers {
  val a = new BankAccount
  val b = new BankAccount

  val c = new Consolidator(List(a,b))

  c.totalBalance
  a deposit 20
  c.totalBalance

  b deposit 30
  c.totalBalance
}