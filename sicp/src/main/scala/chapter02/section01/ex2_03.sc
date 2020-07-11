/*  Exercise 2.3

Implement a representation for rectangles in a plane. (Hint: You may want to make use of exercise 2.2.)
In terms of your constructors and selectors, create procedures that compute the perimeter and the area of a given
rectangle.

Now implement a different representation for rectangles. Can you design your system with suitable abstraction barriers,
so that the same perimeter and area procedures will work using either representation?
 */

case class Point(x: Double, y: Double)

/*
case class Rectangle(width: Double, height: Double) {
  lazy val area = width * height
  lazy val perimeter = 2 * (width + height)
}
val r = Rectangle(3,3)
*/

case class Rectangle(topLeft: Point, bottomRight: Point) {
  lazy val width = bottomRight.x - topLeft.x
  lazy val height = topLeft.y - bottomRight.y
  lazy val area = width * height
  lazy val perimeter = 2 * (width + height)
}

val r = Rectangle(Point(1,4), Point(4, 1))


r.area
r.perimeter
