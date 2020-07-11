import scala.util.Try

/*
Exercise 2.1

Define a better version of make-rat that handles both positive and negative arguments.

Make-rat should normalize the sign so that if the rational number is positive, both the numerator and
denominator are positive, and if the rational number is negative, only the numerator is negative.
 */

def cons[A](x: A, y: List[A]):List[A] = x :: y
def cons[A](x: A, y: A):List[A] = x :: List(y)
def car[A](xs: List[A]): A = xs.head
def cdr[A](xs: List[A]): List[A] = xs.tail

/*def car[A](xs: List[A]): Option[A] = xs.headOption

def cdr[A](xs: List[A]): Option[List[A]] = xs match {
  case Nil | _ :: Nil => None
  case _ :: tail => Some(tail)
}*/

val x = cons(1, 2)
car(x)
cdr(x)

type Rational = List[Int]

def numer(r: Rational): Int = car(r)
def denom(r: Rational): Int = car(cdr(r))

def make_rat(p:Int, q:Int): Rational = (p,q) match {
  case (_, 0) => throw new NumberFormatException("Divide by zero")
  case (0, _) => cons(0, 1)
  case (n, d) if d > 0 => cons(n,d)
  case (n, d) if d < 0 => cons(n * -1, d * -1)
}

make_rat(-1,2)
make_rat(2, -1)
make_rat(0, 1201)

// We could also to this in a much more OOP way

case class ScalaRational(numerator: Int, denominator: Int) {
  import ScalaRational._

  def + (r: ScalaRational): ScalaRational =
    make_rat_scala(numerator*r.denominator + denominator*r.numerator, denominator*r.denominator)

  def + (n: Int): ScalaRational = this.+(ScalaRational(n, 1))

  def - (r: ScalaRational): ScalaRational = this.+(r.*(-1))

  def * (r: ScalaRational): ScalaRational =
    make_rat_scala(numerator*r.numerator, denominator*r.denominator)

  def * (n: Int): ScalaRational =
    make_rat_scala(numerator*n, denominator)

  def / (r: ScalaRational): ScalaRational = this.*(ScalaRational(r.denominator, r.numerator))

  def / (n: Int): ScalaRational =
    make_rat_scala(numerator, denominator*n)

  override def toString: String = numerator + "/" + denominator
}

object ScalaRational {

  @scala.annotation.tailrec
  def gcd(a: Int, b: Int): Int = if (b == 0) a else gcd(b, a % b)

  def make_rat_scala(p: Int, q: Int): ScalaRational = {
    val g = gcd(p,q)
    (p/g, q/g) match {
      case (_, 0) => throw new NumberFormatException("Divide by zero")
      case (0, _) => new ScalaRational(numerator = 0, denominator = 1)
      case (n, d) if d > 0 => new ScalaRational(numerator = n, denominator = d)
      case (n, d) if d < 0 => new ScalaRational(numerator = n * -1, denominator = d * -1)
    }
  }
}


Try {ScalaRational.make_rat_scala(-1,0)}
ScalaRational.make_rat_scala(-1,2)
ScalaRational.make_rat_scala(2,-1)
ScalaRational.make_rat_scala(0,223)
ScalaRational.make_rat_scala(15,220)

val a = ScalaRational(3,4)
val b = ScalaRational(5,8)
a + b

a - b

a * b

a / b



