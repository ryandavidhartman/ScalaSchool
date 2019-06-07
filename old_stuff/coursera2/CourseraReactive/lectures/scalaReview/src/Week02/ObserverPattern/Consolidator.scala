package Week02.ObserverPattern

/**
 * An Observer of BankAccount Publishers.  That is a Subscriber to maintain the total balance
 * of a list of accounts.
 */
class Consolidator(observed: List[BankAccount]) extends Subscriber {
  observed.foreach(_.subscribe(this))

  // Note here the _ means unintialized
  private var total: Int = _
    compute()

  private def compute(): Unit =
    total = observed.map(_.currentBalance).sum

  def handler(pub: Publisher) = compute()

  def totalBalance = total

}
