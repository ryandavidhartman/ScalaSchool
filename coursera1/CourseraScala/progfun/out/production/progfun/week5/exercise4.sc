import week5._

object exercise4 {

  def scaleList(xs: List[Double], factor: Double): List[Double] =
    xs match {
      case Nil => xs
      case y :: ys => y * factor :: scaleList(ys, factor)
    }

    def scaleList2(xs: myList[Double], factor: Double): myList[Double] =
      xs.myMap(x => x*factor)

    def squareList(xs: List[Int]): List[Int] =
      xs match {
        case Nil => xs
        case y :: ys => y * y :: squareList(ys)
      }

    def squareList(xs: myList[Int]): myList[Int] =
      xs.myMap(x => x*x)

    def posElement(xs: List[Int]): List[Int] =
      xs match {
        case Nil => xs
        case y :: ys => if(y>0) y :: posElement(ys) else posElement(ys)
      }
    def posElement2(xs: List[Int]): List[Int] = xs.filter(x => x>0)

  val bob:myList[Int] = 1 :: 2 :: myList(3)
  //val sally:myList[Int] = myList(1) :: myList(2) :: myList(3)
  bob.myFilter(x => x>1)
  bob.myMap(x => x*x)
  val nums = List(2, -4, 5, 7, 1)
  val fruits = List("apple", "pineapple", "orange", "banana")
  nums filter (x => x>0)
  nums filterNot(x => x>0)
  nums partition (x =>x>0)

  def pack[T](xs:List[T]):List[List[T]] = xs match {
    case Nil => Nil
    //case x::xs1 => xs.takeWhile(i=>i==x) :: pack(xs1.dropWhile(j=>j==x))
    case x::xs1 =>
      val(first,rest) = xs span(y => y == x)
      first :: pack(rest)
  }

  def encode[T](xs:List[T]):List[(T,Int)] = {
    def encoder(packed:List[List[T]]):List[(T,Int)] = packed match {
      case Nil => Nil
      case x::xs1 => (x.head,x.length) :: encoder(xs1)
    }
    encoder(pack(xs))
  }

  def encode2[T](xs:List[T]):List[(T,Int)] = {
    pack(xs) map (i => (i.head,i.length))
  }

  val martin = List("a", "a", "a", "b", "c", "c", "a")
  pack(martin)
  encode(martin)

}

