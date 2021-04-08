package section3

object PartialFunctionExercises extends App {

  //
  // Exercise 1:  construct a PF instance yourself by extending the PF trait in some
  // anonymous clas

  val myPf = new PartialFunction[Int, Int] {
    override def isDefinedAt(x: Int): Boolean = Math.abs(x) < 100

    override def apply(x: Int): Int = if (x < 100) x * x else throw new MatchError(s"$x is too large")
  }

  assert(myPf(10) == 100)
  assert(!myPf.isDefinedAt(101))

  //
  // Exercise 2: Make a dump chatbot as a PF
  //

  val myChatBot: PartialFunction[String, String] = {
    case "hi" => "hello there"
    case "bye" => "good bye"
  }

  // this works but crashes on a match error
  //scala.io.Source.stdin.getLines().foreach(l => println(myChatBot(l)))

  scala.io.Source.stdin.getLines().collect(myChatBot).foreach(println)

}




