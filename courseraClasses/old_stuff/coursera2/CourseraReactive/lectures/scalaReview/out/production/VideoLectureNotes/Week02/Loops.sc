
import scala.annotation.tailrec

object Loops {
 /*
 Example an imparative style loop in scala
  */
  def power (x: Double, exp: Int): Double = {
    var r = 1.0
    var i = exp
    while(i>0) {r = r * x; i = i -1}
    r
  }
  /*
  Note the scala 'while' keyword isn't actually necessary
   e.g. we can define a function to do the same thing
   */
  /*
  The condition and the command *must* be passed by name so
  that they're reevaluated in each iteration
  Also note WHILE is tail recursive, so it can operate wiht a
  constant stack size
   */
  def WHILE(condition: => Boolean)(command: => Unit): Unit =
  if(condition) {
    command
    WHILE(condition)(command)
  }
  else ()

  def power2 (x: Double, exp: Int): Double = {
    var r = 1.0
    var i = exp
    WHILE(i>0) {r = r * x; i = i -1}
    r
  }
  val test1 = power(2.0, 4)
  val test2 = power2(2.0, 4)

  /* EXERCISE 1
  Write a function implementing a repeat look that is used as follows:

  REPEAT {
    command
  } ( condition )
   */

  def REPEAT(command: => Unit)(condition: => Boolean): Unit = {
    command
    if(condition) ()
    else REPEAT(command)(condition)
  }

  def power3 (x: Double, exp: Int): Double = {
    var r = 1.0
    var i = exp
    REPEAT{r = r * x; i = i -1}(i < 1)
    r
  }

  val test3 = power3(2.0, 4)


  /* EXERCISE 2
  Is it also possible to obtaint the following syntax?

  REPEAT2 {
    command
  } UNTIL ( condition)
   */

  def REPEAT2(body: => Unit) = new {
    @tailrec
    def UNTIL(condition: => Boolean) {
      body
      if (condition) () else UNTIL(condition)
    }
  }

  def power4 (x: Double, exp: Int): Double = {
    var r = 1.0
    var i = exp
    REPEAT2 {
      r = r * x
      i = i -1
    } UNTIL (i < 1)
    r
  }

  val test4 = power4(2.0, 4)

  /* For-Loops
  The Classical Java for loop can not be modeled simply by a
  higher order function

  for( int i = 1; i<3; i= i+1) { System.out.print(i + " "); }

  Can best be approximated with the following:

  for(i <- 1 until 3) { System.out.print(i + " ") }
   */

  /*
  Translation of For-Loops
  For-loops translate similarly to for-expressions, but they use
  the forach combinnator instead of map and flatMap

  def forEach(f: T => Unit): Unit = apply 'f' to each element
  of the collection
   */
  for(i <- 1 until 3) { println(i + " ") }
  (1 until 3) foreach(i => println(i + ""))

  for(i <- 1 until 3; j <- "abc") { println(i + " " + j) }
  (1 until 3) foreach (i => "abc" foreach(j => println(i + " " + j)))
}