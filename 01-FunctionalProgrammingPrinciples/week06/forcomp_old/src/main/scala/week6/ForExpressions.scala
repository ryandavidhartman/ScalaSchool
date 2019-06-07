package week6

object ForExpressions {

  def isPrime(n: Int): Boolean = (2 to n-1).forall(n % _ != 0)
  def isSumPrime(n1: Int, n2:Int): Boolean = isPrime(n1+n2)
  def combiner(r: Seq[(Int, Int)], z: Seq[(Int, Int)]): Seq[(Int, Int)] = r ++ z

  def main(args: Array[String]): Unit = {
    for (n <- (1 to 10)) {println(s"${n} prime? ${isPrime(n)}")}

    val n = 7
    val seq1 = ((1 to n) map (i => (1 to i) map (j => (i,j)))foldRight Seq[(Int,Int)]())(combiner).filter {case (x,y) => isPrime(x + y)}
    val seq2 = ((1 to n) map (i => (1 to i) map (j => (i,j)))foldRight Seq[(Int,Int)]())(_ ++ _).filter((isSumPrime _).tupled)
    val seq3 = ((1 to n) map (i => (1 to i) map (j => (i,j)))).flatten.filter((isSumPrime _).tupled)
    val seq4 = ((1 to n) flatMap (i => (1 to i) map (j => (i,j)))).filter((isSumPrime _).tupled)

    val seq5 = for {
      i <- 1 to n
      j <- 1 to i
      if isPrime(i+j)
    } yield (i,j)
  }

  def scalarProduct(xs:List[Int], ys: List[Int]): Int = {
    (for ( (x,y) <- xs zip ys) yield (x*y)).sum
  }

}


