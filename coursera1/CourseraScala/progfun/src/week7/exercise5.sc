import week7.Pouring
object exercise5 {
  val problem = new Pouring(Vector[Int](4, 9, 19))
  problem.moves
  problem.pathSets
  problem.pathSets.take(3).toList
  problem.solution(17)


}


