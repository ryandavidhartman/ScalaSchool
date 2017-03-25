package funsets

class Rational3(x: Int, y: Int) {
  require(y !=0, "denominator must be nonzero")

  def this(x:Int) = this(x,1)

  private def gcd(a:Int, b:Int): Int = if (b == 0) a else gcd(b, a%b)
  private val g = gcd(x,y)

  def numer = x / g
  def denom = y / g

  def + (r: Rational3) =
    new Rational3(numer * r.denom + r.numer * denom, denom * r.denom)

  def unary_- : Rational3 = new Rational3(-numer,denom)

  def - (r: Rational3) = this + -r

  def * (r: Rational3) = ???

  def < (that: Rational3): Boolean = numer * that.denom < that.numer * denom

  def max(that:Rational3): Rational3 = if(this < that) that else this

  override def toString = numer + "/" + denom
}
