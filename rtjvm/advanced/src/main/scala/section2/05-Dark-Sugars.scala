package section2

import scala.util.{Failure, Success, Try}
import scala.util.control.NonFatal

object DarkSugars extends App {
  //  Syntax Sugar One: Methods with single parameters

  def singleArgMethod(arg: Int): String= arg.toString  // random method that takes a single argument
  // you can leave off the (), since this can be implied

  val someString = singleArgMethod {
    // ...
    // all kinds of code
    // ...
    // that finally returns a int
    2
  }

  // example  a) The Scala Try.apply takes a single argument
  // Try.apply(arg) => Try(arg) since apply methods can be inferred
  // Try.apply(arg) => Try(arg) => Try arg => Try expression => Try { code in block }

  // This means the Scala Try can look just like the Java try

  val aTryInstance = Try {
    // looks like Java try
    // code
    2
    throw new RuntimeException
  }

  lazy val block = {
    // code
    throw new Exception("dfd")
  }

  val anotherTryInstance: Try[Int] = Try.apply(block)

  val finalTryInstance: Try[Int] = try Success(block) catch {
    case NonFatal(e) => Failure(e)
  }

  // example b)  map, flatMap, and filter => these are single argument functions
  List(1,2,3) map { x =>
    x+1
  }

  List(1,2,3).map(x => x+1)

  // Syntax Sugar Two: single abstract method

  trait Action {
    def act(x: Int): Int
  }

  // don't care what this does, but the second argument is an instance of the Action Trait
  def doAction(thingToActOn: Int,  action: Action): Int = action.act(thingToActOn)

  doAction(10, (r:Int) => r + 1)  // this works!  That lambda can "stand in" for an Action
  // Action is a trait with an single abstract method.  Of type Int => Int.  So anything that is of type
  // Int => Int will work as an action

  val action1: Action = new Action {
    override def act(x: Int): Int = x + 1
  }

  val action2: Action = (x: Int) => x + 1  //Lambda to Single Abstract Method conversion

  // example a:  Runnables
  val thread1: Thread = new Thread(new Runnable {
    override def run(): Unit = println("Very fancy runnable")
  })

  val thread2: Thread = new Thread(() => println("Still fancy"))

  // this also works with Traits/Asstract Classes that have implemented methods:

  trait WithSomeImplementations {
    def add_1(x: Int): Int = x + 1
    def add_2(x: Int): Int = x + 2
    def sammy(s: String): String
  }

  val withSome: WithSomeImplementations = (s: String) => s.toUpperCase
  withSome.add_1(1)



}