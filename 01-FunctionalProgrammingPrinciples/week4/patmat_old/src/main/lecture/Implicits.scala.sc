import math.Ordering

object Implicits {
  def msort[T](xs: List[T])(lt: (T,T) => Boolean): List[T] = {
    val n = xs.length/2
    if(n == 0) xs
    else {

      def merge(xs: List[T], ys: List[T]): List[T] = (xs,ys) match {
        case (List(),ys) => ys
        case (x2,List()) => xs
        case (x :: xs1, y :: ys1) =>
          if (lt(x,y)) x :: merge(xs1, ys)
          else y :: merge(xs, ys1)
      }

      val (fst, snd) = xs splitAt n
      merge(msort(fst)(lt), msort(snd)(lt))
    }
  }

  val test = List(1,99,-6,0,2,1232,493,9)
  val sorted = msort(test)((x:Int, y:Int) => x < y)

  val test2 = List('a', 'z', 'g', 'c')
  val sorted2 = msort(test2)((x:Char, y:Char) => x < y)


  def msort2[T](xs: List[T])(ord: Ordering[T]): List[T] = {
    val n = xs.length/2
    if(n == 0) xs
    else {

      def merge(xs: List[T], ys: List[T]): List[T] = (xs,ys) match {
        case (List(),ys) => ys
        case (x2,List()) => xs
        case (x :: xs1, y :: ys1) =>
          if (ord.lt(x,y)) x :: merge(xs1, ys)
          else y :: merge(xs, ys1)
      }

      val (fst, snd) = xs splitAt n
      merge(msort2(fst)(ord), msort2(snd)(ord))
    }
  }


  val sorted3 = msort2(test)(Ordering.Int)
  val sorted4 = msort2(test2)(Ordering.Char)


  def msort3[T](xs: List[T])(implicit ord: Ordering[T]): List[T] = {
    val n = xs.length/2
    if(n == 0) xs
    else {

      def merge(xs: List[T], ys: List[T]): List[T] = (xs,ys) match {
        case (List(),ys) => ys
        case (x2,List()) => xs
        case (x :: xs1, y :: ys1) =>
          if (ord.lt(x,y)) x :: merge(xs1, ys)
          else y :: merge(xs, ys1)
      }

      val (fst, snd) = xs splitAt n
      merge(msort3(fst), msort3(snd))
    }
  }

  val sorted5 = msort3(test)
  val sorted6 = msort3(test2)
}