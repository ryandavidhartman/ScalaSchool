

def queens(n:Int): Set[List[Int]] = {
  def placeQueens(k: Int): Set[List[Int]] =
    if (k == 0) Set(List())
    else
      for {
        queens <- placeQueens(k-1)
        col <- 0 until n
        if isSafe(col, queens)
      } yield col :: queens

  /* MY SOLUTION
  def isSafe(col:Int, queens:List[Int]): Boolean = queens match {
    case List() => true
    case x :: xs => if(queens.contains(col)) false
                    else checkDiagonal(col, queens,1)
  }

  def checkDiagonal(col:Int, queens:List[Int], step:Int): Boolean = queens match
  {
    case List() => true
    case x :: xs => if(col == x+step || col == x-step) false else checkDiagonal(col, xs, step+1)
  }*/

  //Here is Martin's solution
  def isSafe(col:Int, queens:List[Int]): Boolean = {
    val row = queens.length
    val queensWithRow = (row-1 to 0 by -1) zip queens
    queensWithRow forall {
      case (r,c) => col != c && math.abs(col -c ) != row - r
    }
  }
  placeQueens(n)
}

def show(queens: List[Int]) = {
  val lines =
    for (col <- queens.reverse)
      yield Vector.fill(queens.length)("* ").updated(col, "X ").mkString
  "\n" + (lines.mkString("\n"))
}

queens(1).size
queens(2).size
queens(3).size
queens(4).size
queens(5).size
queens(6).size
queens(7).size
(queens(8) take 3 map show) mkString "\n"

