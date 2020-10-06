package section2

import scala.annotation.tailrec

// // https://ryandavidhartman.github.io/ScalaSchool/Default-Parameters.html

object DefaultArgs extends App {


  /*
  we can avoid, if we wish, a default value for the accumulator
  so we don't really need to use a helper function anymore
   */

  @tailrec
  def fact(n: Int, acc: Int = 1): Int =
    if (n <= 1) acc
    else fact(n - 1, n * acc)


  /*
  Say we have this function and it is usually called with standard parameters
   */
  def savePicture(format: String = "jpg" , width: Int = 640, height: Int = 480): Unit = println("saving picture")

  // in order to call this use NAMED PARAMETERS so the compiler will know what value should go with
  // which parameter

  savePicture(width = 1000)
}
