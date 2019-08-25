def insert(x:Int, xs: List[Int]) : List[Int] = xs match {
  case List() => List(x)
  case y :: ys => {
    if(x <= y) x :: xs
    else y :: insert(x,ys)
  }
}

def iSort(xs: List[Int]) : List[Int] = xs match {
  case List() => List()
  case y :: ys => insert(y, iSort(ys))
}

val bob = 1 :: 3 :: 17 :: 1 :: 4 :: 2 :: Nil
val jim = iSort(bob)
val steve = jim
