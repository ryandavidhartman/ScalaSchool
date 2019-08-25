class Rational1(x: Int, y: Int) {
  require(y != 0, "denominator must be nonzero")
  def this(x: Int) = this(x, 1)
  private def gdc(a: Int, b: Int): Int = if(b == 0) a else gdc(b, a % b)
  private val g = gdc(x,y)
  def numer = x / g
  def denom = y / g
  def less(that: Rational1) = numer*that.denom < that.numer*denom
  def max(that: Rational1) = if(less(that)) that else this
  def add(that: Rational1) =
    new Rational1(
      numer * that.denom + that.numer * denom,
      denom * that.denom
    )
  def neg() = new Rational1(-numer, denom)

  def sub(that: Rational1) =
    add(that.neg())


  override def toString = numer + "/" + denom
}

val x = new Rational1(1, 3)
val y = new Rational1(5, 7)
val z = new Rational1(3, 2)
x.sub(y).sub(z)
y.add(y)
x.less(y)
x.max(y)

x add z
//val strange = new Rational(1,0)
//strange.add(strange)

