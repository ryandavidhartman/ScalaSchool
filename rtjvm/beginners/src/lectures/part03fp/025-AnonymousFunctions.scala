package lectures.part03fp

object AnonymousFunctions extends App {

  val doublerOOP = new Function1[Int, Int] {
    override def apply(x:Int): Int = x*2
 }

  val doublerAnonymous1 = new (Int => Int) {
    override def apply(x:Int): Int = x*2
  }

  /*
  Really though you can just let the compiler figure it out
  for you

  This is the way we usually write anonymous functions
  We also call this a Lambda
   */
  val doublerAnonymous2 = (x:Int) => x*2

  val doublerAnonymous3: Int => Int = x => x*2 //this will work too

  val doublerAnonymous4 = { (x:Int) =>  // you can also put the lambda definition in a block
    x*2
  }

  //How do we do lambdas that take multiple parameters?
  val adderOOP = new Function2[Int, Int, Int] {
    override def apply(x:Int, y:Int):Int =  x+y
  }


  // There are all valid.  #3 is my favorite
  val adderAnonymous1: (Int, Int) => Int = (x:Int,y:Int) => x+y
  val adderAnonymous2: (Int, Int) => Int = (x,y) => x+y
  val adderAnonymous3 = (x:Int,y:Int) => x+y
  val adderAnonymous4 = { (x:Int,y:Int) =>
    x+y
  }

  // How do we do lambdas that have no parameters?
  val justDoSomethingOOP = new Function0[Int] {
    override def apply():Int = 3
  }

  val justDoSomethingAnonymous1: () => Int = () => 3
  val justDoSomethingAnonymous2 = () => 3
  val justDoSomethingAnonymous3 = { () =>
    3
  }

  println(justDoSomethingAnonymous2)  //this is the VAL
  println(justDoSomethingAnonymous2()) // this is the invocation of the lambda!

  // Ok now we can see even more syntactic sugar!
  val niceIncrementer1: Int => Int = (x: Int) => x + 1
  val niceIncrementer2 = (x: Int) => x + 1
  val niceIncrementer3: Int => Int = _ + 1

  val niceAdder1: (Int, Int) => Int = (x:Int, y:Int) => x+y
  val niceAdder1_5: (Int, Int) => Int = (x, y) => x+y
  val niceAdder2: (Int, Int) => Int = _ + _

  val bob1 = niceAdder1(4,5)
  var bob2 = niceAdder1(5,5)

  /**
   * Exercises
   * 1 -> Replace all FunctionX calls with lambdas in the Generic List
     oops I already did that!
   * 2 -> rewrite the "special" adder as an anonymous function
   *
   */

  def bob(x:Int): String = x.toString

}
