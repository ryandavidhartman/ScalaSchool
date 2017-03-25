object exercise3 {
  val fruit = Set("apple", "banana", "pear")
  val s = (1 to 6).toSet

  s map(_ + 2)
  for(i <- s) println(i)
  fruit filter (_.startsWith("app"))
  s.nonEmpty
}