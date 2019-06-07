package lectures.hierarchies

object intsets {
  val t1 = new NonEmpty(3, new Empty, new Empty)
  val t2 = t1 incl 4
}