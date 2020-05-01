/*
Exercise 2.2
Consider the problem of representing line segments in a plane. Each segment is represented as a pair of points: a
starting point and an ending point. Define a constructor make-segment and selectors start-segment and end-segment that
define the representation of segments in terms of points. Furthermore, a point can be represented as a pair of
numbers: the x coordinate and the y coordinate. Accordingly, specify a constructor make-point and selectors x-point
and y-point that define this representation. Finally, using your selectors and constructors, define a procedure
midpoint-segment that takes a line segment as argument and returns its midpoint (the point whose coordinates are the
average of the coordinates of the endpoints). To try your procedures, you'll need a way to print points:

(define (print-point p)
  (newline)
  (display "(")
  (display (x-point p))
  (display ",")
  (display (y-point p))
  (display ")"))
 */

// The easy way:
case class PointEasyWay(x: Double, y: Double)
case class Segment(start: PointEasyWay, end: PointEasyWay) {
  def midpoint() = PointEasyWay((start.x + end.x) / 2.0, (start.y + end.y) / 2.0)
}

// The Scheme like way?

def cons[A](x: A, y: List[A]):List[A] = x :: y
def cons[A](x: A, y: A):List[A] = x :: List(y)
def car[A](xs: List[A]): A = xs.head
def cdr[A](xs: List[A]): List[A] = xs.tail

type Point = List[Double]

def average(a: Double, b: Double): Double = (a+b)/2.0

def make_point(x: Double, y: Double): Point = cons(x,y)
def x_point(p: Point):Double = car(p)
def y_point(p: Point):Double = cdr(p).head
