object exercise5 {

  def isort(xs: List[Int]) : List[Int] = xs match {
    case List() => List()
    case y :: ys => insert(y, isort(ys))
  }

  def insert(x:Int, xs: List[Int]) : List[Int] = xs match {
    case List() => List(x)
    case y :: ys => {
      if(x <= y) x :: xs
      else y :: insert(x,ys)
    }
  }

  val bob = 1 :: 3 :: 17 :: 1 :: 4 :: 2 :: Nil
  val jim = isort(bob)
  val steve = jim
}