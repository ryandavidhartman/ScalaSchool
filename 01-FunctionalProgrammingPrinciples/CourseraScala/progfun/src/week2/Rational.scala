package week2

/**
 * Created by ryan on 4/6/15.
 */
class Rational(x: Int, y: Int) {
  require(y != 0, "denominator must be nonzero")
  def this(x: Int) = this(x, 1)
  private def gdc(a: Int, b: Int): Int = if(b == 0) a else gdc(b, a % b)
  private val g = gdc(x,y)

  val numer = x / g
  val denom = y / g

  def < (that: Rational) : Boolean = {
    numer * that.denom < that.numer * denom
  }

  def max(that: Rational): Rational = {
    if (this < that) that else this
  }

  def + (that: Rational):Rational = {

    new Rational(
      numer * that.denom + that.numer * denom,
      denom * that.denom
    )
  }

  def * (that: Rational) : Rational = {
    new Rational(numer*that.numer, denom*that.denom)
  }

  def unary_- :Rational = new Rational(-numer, denom)

  def - (that: Rational) = this + -that


  override def toString = numer + "/" + denom
}
