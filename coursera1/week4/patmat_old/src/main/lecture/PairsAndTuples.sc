object PairsAndTuples {

  def mergeSort(xs: List[Int]): List[Int] = {
    val n = xs.length/2
    if(n == 0) xs
    else {
      val (fst, snd) = xs splitAt n
      merge2(mergeSort(fst), mergeSort(snd))
    }
  }

  def merge(xs: List[Int], ys: List[Int]): List[Int] = xs match {
    case List() => ys
    case x :: xs1 => ys match {
      case List() => xs
      case y :: ys1 =>
        if (x < y) x :: merge(xs1, ys)
        else y :: merge(xs, ys1)
    }
  }

  def merge2(xs: List[Int], ys: List[Int]): List[Int] = (xs,ys) match {
    case (List(),ys) => ys
    case (x2,List()) => xs
    case (x :: xs1, y :: ys1) =>
      if (x < y) x :: merge(xs1, ys)
      else y :: merge(xs, ys1)
    }

  val test = List(1,99,-6,0,2,1232,493,9)
  val sorted = mergeSort(test)
  
}