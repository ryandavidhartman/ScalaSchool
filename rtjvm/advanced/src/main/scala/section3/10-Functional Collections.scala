package section3

object FunctionalCollections extends App {

  val set = Set(1, 2, 3)  // An instance of the Set class from the standard Scala collections library

  // Since Set has an apply method, we can "call" a set.  Here the apply method returns a boolean
  // describing whether or not the value of the input parameter is in the set.

  println(s"Is 0 in the set? ${set(0)},  Is 1 in the set? ${set(1)}")

  // The point is a Set is callable like a function!  So if a collection is callable like a function,
  // why not actually define a collection in terms of a function?

  // BTW is actually is how Sets are defined in Scala.  trait Set[A] extends (A) => Boolean with {other stuff}
  // So the actual type definition of a set in Scala is a function type.

  // Lets play around with this ourselves.  Exercise Functional Set
  // Implement this trait:  trait MySet[T] extends (T => Boolean)
  //   It should have: contains(), +, ++, map, flatMap, filter, and foreach

  trait MySet[T] extends (T => Boolean) {
    def contains(e: T): Boolean = this(e)
    def +(e: T): MySet[T] = (x: T) => this(x) || x == e
    def ++(s: MySet[T]): MySet[T] = (x: T) => this(x) || s(x)
    def map[U](f: T => U): MySet[U] = ???
  }


}