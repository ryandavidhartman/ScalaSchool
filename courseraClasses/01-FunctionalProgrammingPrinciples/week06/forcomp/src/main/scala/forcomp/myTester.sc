

type Occurrences = List[(Char, Int)]
val abba:Occurrences = List(('a', 2), ('b', 2))
def combinations(occurrences: Occurrences): List[Occurrences] = {
  val expanded: List[Occurrences] = occurrences map (x => (for (i <- (1 to x._2)) yield (x._1, i)).toList)
  //println(expanded)
  expanded.foldRight(List[Occurrences]((Nil)))((x,y) => y ++ (for(i <- x; j <- y; debug = println(s"In combination func y:$y x:$x i:$i j:$j cons:${i::j}")) yield (i :: j)))

}

val combos = combinations(abba)
println(s"we have ${combos.length} combos")

combos.foreach { os =>
  os.foreach(o => println(s"character: ${o._1} occurrences: ${o._2}"))
}
