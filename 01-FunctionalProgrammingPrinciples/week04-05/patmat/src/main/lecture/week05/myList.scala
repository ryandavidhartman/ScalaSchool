package week5

trait myList[+T] {
  def isEmpty: Boolean

  def head: T

  def tail: myList[T]

  def prepend[W >: T](elem: W) = new myCons[W](elem, this)

  def ::[Z >: T](x: Z) = new myCons[Z](x, this)

  def myMap[U](f: T => U): myList[U] = this match {
    case myCons(x, xs) => f(x) :: xs.myMap(f)
    case myEmpty => this.asInstanceOf[myList[U]]
  }

  def myFilter(p: T => Boolean): myList[T] = this match {
    case myCons(x, xs) => if (p(x)) x :: xs.myFilter(p) else xs.myFilter(p)
    case myEmpty => this
  }

  def myFoldLeft[R](accumulator: R)(op: (R, T) => R): R = this match {
    case myCons(x, xs) => (xs myFoldLeft op(accumulator, x))(op)
    case myEmpty => accumulator
  }

  def myReduceLeft[Z >: T](op: (Z, Z) => Z): Z = this match {
    case myCons(x, xs) => xs.myFoldLeft[Z](x)(op)
    case myEmpty => throw new Error("Nil.reduceLeft")
  }

  def myFoldRight[R](accumulator: R)(op: (T, R) => R): R = this match {
    case myCons(x, xs) => op(x, xs.myFoldRight(accumulator)(op))
    case myEmpty => accumulator
  }

  def myReduceRight[Z >: T](op: (Z, Z) => Z): Z = this match {
    case myCons(x, xs) => if (xs.isEmpty) x else op(x, xs.myReduceRight(op))
    case myEmpty => throw new Error("Nil.reduceLeft")
  }

  def myConcat[Q >: T](ys: myList[Q]): myList[Q] = (this myFoldRight ys)(_ :: _)

  def myMap2[Q >: T](f: T => Q): myList[Q] = (this myFoldRight new myEmptyList[Q].asInstanceOf[myList[Q]])(f(_) :: _)

  def myLength[Q >: T]():Int = (this myFoldRight 0)((x:Q,y:Int) => 1+y)
}

class myEmptyList[T] extends myList[T]
{
  def isEmpty: Boolean = true
  def head = throw new Error("Empty List")
  def tail = throw new Error("Empty List")
}

object myEmpty extends myList[Nothing] {
  def isEmpty: Boolean = true
  def head = throw new Error("Empty List")
  def tail = throw new Error("Empty List")
}

case class myCons[T](val head: T, val tail: myList[T]) extends myList[T] {
  def isEmpty: Boolean = false
  override  def toString = if(tail.isEmpty) head.toString
  else head.toString + ", " + tail.toString
}

object myList {
  def apply[T] : myList[T] = myEmpty
  def apply[T](x:T) : myList[T] = new myCons(x, apply)
  def apply[T](x: T, y: T) : myList[T] = new myCons(x, apply(y))
  def apply[T](x:T, y:T, z:T) : myList[T] = new myCons(x, apply(y,z))
}