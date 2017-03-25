class Rational2(x: Int, y: Int) {
  require(y !=0, "denominator must be nonzero")

  def this(x:Int) = this(x,1)
 
  private def gcd(a:Int, b:Int): Int = if (b == 0) a else gcd(b, a%b)
    
  val numer = x
  val denom = y

  def add(r: Rational2) =
    new Rational(numer * r.denom + r.numer * denom, denom * r.denom)

  def neg: Rational2 = new Rational2(-numer,denom)

  def sub(r: Rational2) = add(r.neg)

  def mul(r: Rational2) = ???

  def less(that: Rational2): Boolean = numer * that.denom < that.numer * denom

  def max(that:Rational2): Rational2 = if(less(that)) that else this

  override def toString = {
    val g = gcd(x,y)
    s"${numer/g}/${denom/g}"
  }
}