package week04.idealized.scala


abstract class Boolean {
  def ifThenElse[T](t: => T, e: => T): T

  def && (x: => Boolean): Boolean = ifThenElse(x, False)

  def || (x: => Boolean): Boolean = ifThenElse(True, x)

  def unary_! = ifThenElse(False, True)

  def == (x: Boolean): Boolean = ifThenElse(x, x.unary_!)

  def != (x: Boolean): Boolean = ifThenElse(x.unary_!, x)

  def < (x: Boolean): Boolean = ifThenElse(False, x)

  def > (x: Boolean): Boolean = ifThenElse(x.unary_!, False)

  def <= (x: Boolean): Boolean = ifThenElse(x, True)

  def >= (x: Boolean): Boolean = ifThenElse(True, x.unary_!)
}

object True extends Boolean {
  def ifThenElse[T](t: => T, e: => T) = t
}

object False extends Boolean {
  def ifThenElse[T](t: => T, e: => T) = e
}