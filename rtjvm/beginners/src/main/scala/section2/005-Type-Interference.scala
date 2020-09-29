package section2

// https://ryandavidhartman.github.io/ScalaSchool/Type-Inference.html

object TypeInterference extends App {

  val x1: Int = 1
  val x2 = 1

  def foo(name: String) = s"hi $name"

  // type required for recursive functions
  def sum1ToN(n: Int): Int = if(n<=1) 1 else n + sum1ToN(n-1)
  
}
