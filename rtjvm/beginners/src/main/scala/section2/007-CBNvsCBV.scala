package section2

// https://ryandavidhartman.github.io/ScalaSchool/Evaluation-Rules.html

object CBNvsCBV extends App {

  def callByValue(x: Long): Unit = {
    println(s"callByValue: $x")
    println(s"callByValue: $x")
  }

  def callByName(x: => Long): Unit = {
    println(s"callByName: $x")
    println(s"callByName: $x")
  }

  callByValue(System.nanoTime())
  callByName(System.nanoTime())

  def inf: Int = inf + 1
  def printFirst(x: Int, y: => Int): Unit = println(x)

  // compiles but blows up printFirst(inf, 34)
  printFirst(34, inf)  //cool because inf is never evaluated


}
