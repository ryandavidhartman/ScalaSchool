
import week4._

object exercise2 {
  val a: Array[NonEmpty] = Array(new NonEmpty(1, Empty, Empty))
  //val b: Array[IntSet] = a Array isn't covariant!
  val bob = new Succ(Zero)
  val sally = new Succ(bob)
  val mary = bob + sally
  val jim = mary - bob
  val steve = mary + mary + mary
  jim-steve
}