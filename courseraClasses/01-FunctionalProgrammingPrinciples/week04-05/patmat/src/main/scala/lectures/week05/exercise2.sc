

  def msort(xs:List[Int]): List[Int] = {
    val n = xs.length/2
    if(n == 0) xs
    else {
      val (fst, snd) = xs splitAt n
      merge2(msort(fst), msort(snd))
    }
  }

  def merge(xs: List[Int], ys:List[Int]): List[Int]= {
    xs match {
      case Nil =>
        ys
      case x:: xs1 =>
        ys match {
          case Nil =>
            xs
          case y :: ys1 =>
            if(x<y) x :: merge(xs1, ys)
            else y :: merge(xs, ys1)
        }
    }
  }

  def merge2(xs: List[Int], ys:List[Int]): List[Int]= {
    (xs,ys) match {
      case (Nil,ys) => ys
      case(xs,Nil) => xs
      case (x:: xs1, y::ys1) => if(x<y) x :: merge2(xs1, ys)
                                else y :: merge2(xs, ys1)
    }
  }


  val bob = List(17,3,2)
  val tim = List(9,6,7)
  merge(bob, tim)
  merge(tim, bob)
  val bobs = List(2,3,17)
  val tims = List(6,7,9)
  merge(bobs, tims)
  val pair = ("answer", 42)
  val (label, value) = pair
  value+1

  val test = List(2,232,23,1,-1)
  msort(test)
