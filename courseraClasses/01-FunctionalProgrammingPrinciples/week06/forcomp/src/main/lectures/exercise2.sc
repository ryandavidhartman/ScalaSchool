
val n = 7
val pairs: IndexedSeq[Seq[(Int, Int)]] = {

  (1 until n) map (i => (1 until i) map (j => (i, j)))
}
val bob0 = pairs
val bob1 = bob0.foldRight(Seq[(Int, Int)]())(_ ++ _)
val bob2 = pairs.flatten
val bob3 = {
  val n = 7
  (1 until n) flatMap (i => (1 until i) map (j => (i, j)))
}

def isPrime(x: Int): Boolean = (2 until x) forall (x % _ != 0)

assert(bob2 == bob3)

val bob4 = bob3 filter (pair => isPrime(pair._1 + pair._2))

bob4 map (i => println(s"${i._1},${i._2}"))
case class Person(name: String, age: Int)
val testData = List(Person("Ryan", 45), Person("Bob", 12), Person("Sally", 21))
val filter1 = testData filter (p => p.age > 20) map (p => p.name)
val filter2 = for (p <- testData if p.age > 20) yield p.name
val bob5 = for {i <- (1 until n)
                j <- (1 until i)
                if (isPrime(i + j))
} yield (i, j)
def scalarProduct(v1:Vector[Double], v2:Vector[Double]):Double = (for
  ((x,y) <- v1 zip v2) yield x*y).sum

val v2 = Vector(2.0,2.0,2.0)
val v1 = Vector(1.0,1.0,1.0)

scalarProduct(v1,v2)

