package lecture

/**
  * How function types relate to classes, and how function values relate to objects
  */

/**
  * In fact function values are treated as objects in Scala.
  *
  * The function type A => B is just an abbreviation for the class
  * scala.function1[A,B]  which is roughly
  */

trait Function1[A,B] {
  def apply(x:A): B
}

/* So functions are objects with apply methods
   there are traits Function2, Function3 etc.
 */

/* An anonymous fucntion such as (x:Int) => x*x

is expanded to:
 */

object blockA {

  {
    class AnonFun extends Function1[Int, Int] {
      def apply(x: Int) = x * x
    }
    new AnonFun
  }
}

/* we don't actually need the class name anywhere so
   we can use the anonymous class syntax
 */

object blockB {

  {
    new Function1[Int, Int] {
      def apply(x: Int) = x * x
    }
  }
}

/**
  * Expansion of Function Calls
  *
  * A function call, such as f(a,b), where f is a value of some class type, is expanded to
  *
  *   f.apply(a,b)
  *
  */

object blockC {

  val f = (x:Int) => x * x
  val bob = f(7)

  // translates to:
  val f2 = new Function1[Int, Int] {
    def apply(x:Int) = x * x
  }

  val bob2 = f2.apply(7)
}