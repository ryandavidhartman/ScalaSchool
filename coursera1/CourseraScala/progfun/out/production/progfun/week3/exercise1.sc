import week3._

object exercise1 {
  val t1 = new NonEmpty(3, Empty, Empty)
  println(t1)
  val t2 = t1 incl 4
  val t3 = new NonEmpty(1, Empty, Empty)
  val t4 = t2 union t3
  val t4Test = t2.union(t3)
  val t5 = t3 union t2
}
