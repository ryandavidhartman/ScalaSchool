package week4


abstract class Nat {
  def isZero: Boolean
  def predecessor: Nat
  def successor: Nat = new Succ(this)
  def + (that: Nat):Nat
  def - (that: Nat):Nat
}

object Zero extends Nat {
  def isZero: Boolean = true
  def predecessor: Nat = throw new ArithmeticException("Zero has no predecessor")
  def + (that: Nat):Nat = that
  def - (that: Nat):Nat = if(that.isZero) Zero else throw new ArithmeticException("substraction leads to negative number")
  override def toString = "0"
}

class Succ(n:Nat) extends Nat {
  def isZero: Boolean = false
  def predecessor: Nat = n
  def + (that: Nat):Nat = new Succ(n + that)
  def - (that: Nat):Nat = if(that.isZero) this else n - that.predecessor

  override def toString = {

    def loop(acc:Int, x:Nat): String = {
      if(x.isZero) acc.toString else loop(acc+1,x.predecessor)
    }

    loop(0,this)
  }
}
