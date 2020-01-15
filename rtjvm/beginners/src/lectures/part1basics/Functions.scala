package lectures.part1basics

object Functions extends App {

  //How to define a function
  def aFunction(a:String, b:Int): String = a + " " + b

  println(aFunction("hello", 3))

  // Note parameterless functions can be called without ()
  def aParameterlessFuncton(): Int = 42
  println(aParameterlessFuncton())
  println(aParameterlessFuncton)

 //How can we do loops in a more functional way?  (i.e. avoid using loops



}
