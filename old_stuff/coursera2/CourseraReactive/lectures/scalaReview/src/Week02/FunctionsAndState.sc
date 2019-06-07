package Week02

object FunctionsAndState {
  /*
  Remember the substition rule
   */
  def iterate(n: Int, f: Int => Int, x: Int): Int =
    if (n < 1) x else iterate(n - 1, f, f(x))
  def square(x: Int): Int = x * x
  val test1 = iterate(1, square, 3)
  // this gets rewritten as follows
  val iter1 = if (1 < 1) 3 else iterate(1 - 1, square, square(3))
  val iter2 = iterate(0, square, square(3))
  val iter3 = iterate(0, square, 3 * 3)
  val iter4 = iterate(0, square, 9)
  val iter5 = if (0 < 1) 9 else iterate(0 - 1, square, square(9))
  val iter6 = 9
  /* Note Rewriting can be done anywhere in a term, and all rewritings
  which terminate lead to the same solution.
  This is an important result of the lambda-calculus. Church-Rosser Theorem
  */
  val step0 = if (1 < 1) 3 else iterate(1 - 1, square, square(3))
  val step1a = iterate(1 - 1, square, square(3))
  val step1b = if (1 < 1) 3 else iterate(1 - 1, square, 3 * 3)
  /* We could have moved from step0 to step1a or step1b,
   But either way we get the same result because we are dealing with PURE
   FUNCTIONS.
   */
  /* Objects have a state if its behavior is influenced by its history
     Can I withdraw $100 from my checking account depends on when you ask
   */

  class BankAccount {
    private var balance = 0

    def deposit(amount: Int): Unit = {
      if (amount > 0) balance = balance + amount
    }

    def withdraw(amount: Int): Int =
      if (0 < amount && amount <= balance) {
        balance = balance - amount
        balance
      } else throw new Error("insufficient funds")
  }
  val account = new BankAccount
  account deposit 50
  account withdraw 20
  account withdraw 20
  //account withdraw 15 -> exception


}