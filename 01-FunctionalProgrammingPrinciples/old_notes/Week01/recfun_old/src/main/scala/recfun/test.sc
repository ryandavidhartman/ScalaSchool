import recfun.Main._

object test {

  def pascal(c: Int, r: Int): Int = {
    if(c == 0)
      1
    else if(c == r)
      1
    else
      pascal(c-1,r-1) + pascal(c,r-1)

  }

  pascal(1,3)
  pascal(0,2)
  pascal(1,2)

  val bob = countChange(4, List(1,2))





}