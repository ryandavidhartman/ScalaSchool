class Rational(x: Int, y: Int) {
  require(y !=0, "denominator must be nonzero")

  def this(x:Int) = this(x,1)
 
  private def gcd(a:Int, b:Int): Int = if (b == 0) a else gcd(b, a%b)
  private val g = gcd(x,y)
  
  def numer = x / g
  def denom = y / g

  def add(r: Rational) =
    new Rational(numer * r.denom + r.numer * denom, denom * r.denom)

  def neg: Rational = new Rational(-numer,denom)

  def sub(r: Rational) = add(r.neg)

  def mul(r: Rational) = ???

  def less(that: Rational): Boolean = numer * that.denom < that.numer * denom

  def max(that:Rational): Rational = if(less(that)) that else this

  override def toString = numer + "/" + denom
}