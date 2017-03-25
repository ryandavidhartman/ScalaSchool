package week6

object nQueens {
  def queens(n: Int): Set[List[Int]] = {

    def isSafe(col: Int, queens: List[Int]): Boolean = {
      val row = queens.length
      val previousSolution = (row-1 to 0 by -1) zip queens
      previousSolution forall {
        case (r, c) => col != c && math.abs(col-c) != math.abs(row - r)
      }
    }

    def placeQueens(k:Int): Set[List[Int]] =
      if(k ==0) Set(List())
      else
        for {
          queens <- placeQueens(k-1)
          col <- 0 until n if isSafe(col, queens)
        } yield col :: queens

    placeQueens(n)
  }

  def show(queens: List[Int]) = {
    val lines =
      for(col <- queens.reverse)
        yield Vector.fill(queens.length)("* ").updated(col, col + " ").mkString
    "\n" + (lines mkString "\n")
  }

  def main(args: Array[String]) {
    println((queens(8) map show) mkString "\n")
  }
}
