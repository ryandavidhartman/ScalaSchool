package section4

object WhatsAFunction extends App {

  // DREAM: use functions as first class elements.  This means working with functions like
  // we work with plain values.

  // PROBLEM: this is a JVM based language, and it is fundamentally an OOP language.  This means
  // basically everything is an instance of some class.

  // Example classic Java
  class Action {
    def execute(element: Int): String = ???
  }

  // We can do a little better in Java, still by using generics and a trait.
  trait Action2[A,B] {
    def execute(element: A): B = ???
  }

  // So this is basically what Scala does.  i.e. our functions are really just instances of
  // some anonymous class.
  trait MyFunction[A,B] {
    def apply(element: A): B
  }

  val doubler = new MyFunction[Int, Int] {
    def apply(element: Int): Int = element *2
  }

  val bob = doubler(5)  // see how we can call doubler like a function!
  println(bob)

  // So Scala just basically provides these "function" traits for us!
  // They are called Function0, Function1, Function2 etc.

  val stringToIntConverter1: Function1[String, Int] = new Function1[String, Int] {
    override def apply(s: String): Int = s.toInt
  }

  println(stringToIntConverter1("3") + 4)

  // String => Int is syntactic sugar for  Function1[String, Int]:
  val stringToIntConverter2: String => Int = new (String => Int) {
    override def apply(s: String): Int = s.toInt
  }

  // Note since this is a Function1 is a "SAM" we can just do this:
  val stringToIntConverter3: String => Int = (s: String) => s.toInt

  // Lets take this from start to finish, with another example:

  // Step 1:  explicitly implement Function2 trait:
  // recall  Function2[A,B,R] === (A,B) => R
  val adder1: (Int, Int) => Int = new Function2[Int, Int, Int] {
    override def apply(v1: Int, v2: Int): Int = v1 + v2
  }


  // Step 2:  remember (Int, Int) => Int == Function2[Int, Int, Int]
  val adder2: ((Int, Int) => Int) = (v1: Int, v2: Int) => v1 + v2

  // Step 3: remember Function2 is a SAM
  val adder3 = (v1: Int, v2: Int) => v1 + v2


  // Step 4: compare to def!
  def addr4(v1:Int, v2:Int) = v1 + v2
}
