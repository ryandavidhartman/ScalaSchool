import  week5._


  def mySum1(xs: List[Int]): Int = xs match {
    case Nil => 0
    case x :: xs1 => x + mySum1(xs1)
  }

  def myProd1(xs: List[Int]): Int = xs match {
    case Nil => 1
    case x :: xs1 => x * myProd1(xs1)
  }
  def mySum2(xs: List[Int]): Int = (0::xs) reduceLeft((x,y)=>x+y)
  def myProd2(xs: List[Int]): Int = (1::xs) reduceLeft((x,y)=>x*y)
  def mySum3(xs: List[Int]): Int = (0::xs) reduceLeft(_+_)
  def myProd3(xs: List[Int]): Int = (1::xs) reduceLeft(_*_)
  def mySum4(xs: List[Int]): Int = (xs foldLeft 0)(_+_)
  def myProd4(xs: List[Int]): Int = (xs foldLeft 0)(_*_)
  val ryan1 = myList(1,2)
  val ryan2 = myList(3,4)
  val ryan3 = ryan1 myConcat ryan2

  ryan1 myMap (x => x+4)

  ryan1.myMap2(x => x*5)

  ryan3 myLength

  myEmpty.myLength()
