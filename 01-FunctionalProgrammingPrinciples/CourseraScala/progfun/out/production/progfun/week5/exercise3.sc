import math.Ordering
object exercise3 {
  def msort[T](xs:List[T])(lt:(T,T) => Boolean): List[T] = {
    val n = xs.length/2
    if(n == 0) xs
    else {
      val (fst, snd) = xs splitAt n
      merge(msort(fst)(lt), msort(snd)(lt))(lt)
    }
  }

  def merge[T](xs: List[T], ys:List[T])(lt:(T,T) => Boolean): List[T]= {
    (xs,ys) match {
      case (Nil,ys) => ys
      case(xs,Nil) => xs
      case (x:: xs1, y::ys1) => if(lt(x,y)) x :: merge(xs1, ys)(lt)
      else y :: merge(xs, ys1)(lt)
    }
  }
  val nums = List(34, 5, 4, 2, 99,-1)
  msort(nums)((x:Int, y:Int) => x < y)
  val fruits = List("apple", "pineapple", "orange", "banana")
  msort(fruits)((x:String, y:String) => x.compareTo(y) <0 )

  def msort2[T](xs:List[T])(ord: Ordering[T]): List[T] = {
    val n = xs.length/2
    if(n == 0) xs
    else {
      val (fst, snd) = xs splitAt n
      merge2(msort2(fst)(ord), msort2(snd)(ord))(ord)
    }
  }

  def merge2[T](xs: List[T], ys:List[T])(ord:Ordering[T]): List[T]= {
    (xs,ys) match {
      case (Nil,ys) => ys
      case(xs,Nil) => xs
      case (x:: xs1, y::ys1) => if(ord.lt(x,y)) x :: merge2(xs1, ys)(ord)
      else y :: merge2(xs, ys1)(ord)
    }
  }
  msort2(nums)(Ordering.Int)
  msort2(fruits)(Ordering.String)

  def msort3[T](xs:List[T])(implicit ord: Ordering[T]): List[T] = {
    val n = xs.length/2
    if(n == 0) xs
    else {
      val (fst, snd) = xs splitAt n
      merge3(msort3(fst), msort3(snd))
    }
  }

  def merge3[T](xs: List[T], ys:List[T])(implicit ord:Ordering[T]): List[T]= {
    (xs,ys) match {
      case (Nil,ys) => ys
      case(xs,Nil) => xs
      case (x:: xs1, y::ys1) => if(ord.lt(x,y)) x :: merge3(xs1, ys)
      else y :: merge3(xs, ys1)
    }
  }
  msort3(nums)(Ordering.Int)
  msort3(fruits)(Ordering.String)
  msort3(nums)
  msort3(fruits)


}

