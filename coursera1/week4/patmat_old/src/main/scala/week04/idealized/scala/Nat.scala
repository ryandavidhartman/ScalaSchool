package week04.idealized.scala

/**
  * These are Peano Numbers
  *
  * a + 0 = a
  * a + S(b) = S(a+b)  OR this + that = Succ(n) + that = Succ(n + that)
  */

abstract class Nat {
  def isZero: Boolean
  def predecessor: Nat
  def successor: Nat
  def + (that: Nat): Nat
  def - (that: Nat): Nat
}

object Zero extends Nat {
  def isZero: Boolean = True
  def predecessor: Nat = throw new NoSuchElementException("0.predecessor")
  def successor: Nat = new Succ(this)
  def +(that: Nat): Nat = that
  def -(that: Nat): Nat = that.isZero.ifThenElse(Zero, throw new NoSuchElementException("subtraction from 0"))
}

case class Succ(n: Nat) extends Nat {
  def isZero: Boolean = False
  def predecessor: Nat = n
  def successor: Nat = new Succ(this)
  def +(that: Nat): Nat = new Succ(n + that)
  def -(that: Nat): Nat = that.isZero.ifThenElse(this, n - that.predecessor)
}
