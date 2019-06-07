//package week1
object funs {

  val f: (Int => String) = List("a", "b", "c")
  f(2)
  
  val fun: PartialFunction[String, String] = { case "ping" => "pong" }
  fun("ping")
  fun.isDefinedAt("ping")
  fun.isDefinedAt("bob")

}