package section2

import scala.util.{Failure, Success, Try}
import scala.util.control.NonFatal

object DarkSyntaxSugar extends App {

  /////////////////////////////////////////////////////////////////////////////
  //  Syntax Sugar One: Methods with single parameters
  /////////////////////////////////////////////////////////////////////////////

  def singleArgMethod(arg: Int): String= arg.toString  // random method that takes
  // a single argument you can leave off the () using supply a code block instead

  val someString = singleArgMethod { if(true) 5 else 1 }

  val someOtherString = singleArgMethod {
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
    //looks like Java try
    //  ... code ...
    throw new RuntimeException("whoops")
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

  /////////////////////////////////////////////////////////////////////////////
  // Syntax Sugar Two: single abstract method
  /////////////////////////////////////////////////////////////////////////////

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

  // this also works with Traits/Abstract Classes that have implemented methods:

  trait WithSomeImplementations {
    def add_1(x: Int): Int = x + 1
    def add_2(x: Int): Int = x + 2
    def sammy(s: String): String
  }

  val withSome: WithSomeImplementations = (s: String) => s.toUpperCase
  withSome.add_1(1)

  /////////////////////////////////////////////////////////////////////////////
  // Syntax Sugar Three: Methods ending with : are RIGHT ASSOCIATIVE
  /////////////////////////////////////////////////////////////////////////////

  val prependedList = 2 :: List(3,4)  // this is really List(3,4).::(2) NOT 2.::(List(3,4))

  val prependedList2 = List(3,4).::(2)

  class MyStream[T] {
    def -->:(value: T): MyStream[T] = this  // some implementation
  }

  val myStream = 1 -->: 2 -->: 3 -->: new MyStream[Int]()


  /////////////////////////////////////////////////////////////////////////////
  // Syntax Sugar Four: Multi-word method naming
  /////////////////////////////////////////////////////////////////////////////

  class Speaker(name: String) {
    def `once said`(quotation: String): Unit = println(s"name said $quotation")
  }

  val lilly = new Speaker("Lilly")
  lilly `once said` "life is sweat sorrow"

  /////////////////////////////////////////////////////////////////////////////
  // Syntax Five: Infix Types
  /////////////////////////////////////////////////////////////////////////////

  class Composite[A, B](a: A, b: B)  // Generic type with two class parameters
  val composite1: Composite[Int, String] = new Composite(4, "four")
  val composite2: Int Composite String = new Composite(4, "four")

  abstract class -->[A, B] {
    def to(a:A): B
  }
  val towards: Int --> String = (x:Int) => x.toString
  // We'll this this again with type level programming

  /////////////////////////////////////////////////////////////////////////////
  // Syntax Six: like apply() update is very special
  /////////////////////////////////////////////////////////////////////////////

  val anArray: Array[Int] = Array(1,2,3)
  anArray(2) = 7  // this is the same anArray.update(2,7)

  // this is a standard facility in mutable data structures

  /////////////////////////////////////////////////////////////////////////////
  // Syntax Seven: mutable Data Types have setters special methods
  // ending wtih _=
  /////////////////////////////////////////////////////////////////////////////

  class MyMutableWrapper[T](private var v: T) {
    def data: T = data
    def data_=(value: T): Unit = v = value
  }

  val myMutableWrapper = new MyMutableWrapper(0)

  val useGetter = myMutableWrapper.data
  // use the setter
  myMutableWrapper.data = 7



}