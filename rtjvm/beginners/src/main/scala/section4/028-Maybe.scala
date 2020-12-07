package section4

abstract class Maybe[+T] {
  def isEmpty: Boolean
  def get(): T
  def map[U](f: T => U): Maybe[U]
  def flatMap[U](f: T => Maybe[U]): Maybe[U]
  def filter(p: T => Boolean): Maybe[T]
}

case object Nope extends Maybe[Nothing] {
  def isEmpty: Boolean = true
  def get(): Nothing = throw new NoSuchElementException("Nope is empty!")
  def map[U](f: Nothing => U): Maybe[U] = Nope
  def flatMap[U](f: Nothing => Maybe[U]): Maybe[U] = Nope
  def filter(p: Nothing => Boolean): Maybe[Nothing] = Nope
}

case class Yep[+T](value: T) extends Maybe[T] {
  def isEmpty: Boolean = false
  def get(): T = value
  def map[U](f: T => U): Maybe[U] = Yep(f(value))
  def flatMap[U](f: T => Maybe[U]): Maybe[U] = f(value)
  def filter(p: T => Boolean): Maybe[T] = if(p(value)) this else Nope
}

object MaybeRunner extends App {
  val nope: Maybe[Int] = Nope
  println(nope.map(x => x *2))

  val maybe1 = Yep(1)
  val maybe2 = Yep(2)

  val results1 = for {
    i1 <- maybe1
    i2 <- maybe2
  } yield i1 + i2

  val results2 = maybe1.flatMap(i1 => maybe2.map(i2 => i1 + i2))
  assert(results1 == results2)

  println(s"results: $results1")
}


