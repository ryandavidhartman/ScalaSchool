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
def demon(r: Rational): Int = cdr(r).head

def make_rat(p:Int, q:Int): Rational = (p,q) match {
  case (_, 0) => throw new NumberFormatException("Divide by zero")
  case (0, _) => cons(0, 1)
  case (n, d) if d > 0 => cons(n,d)
  case (n, d) if d < 0 => cons(n * -1, d * -1)
}

make_rat(-1,2)
make_rat(2, -1)
make_rat(0, 1201)



