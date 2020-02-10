package lectures.part3fp

object WhatsAFunction extends App {

  // DREAM: use functions as first class elements
  // PROBLEM: this is a JVM based language, and it is fundamentally an OOP language

  // Example classic Java
  trait Action[A,B] {
    def execute(element: A): B = ???
  }

  // So this is basically what Scala does
  trait MyFunction[A,B] {
    def apply(element: A): B
  }

  val doubler = new MyFunction[Int, Int] {
    def apply(element: Int): Int = element *2
  }

  val bob = doubler(5)
  println(bob)

  // So Scala just basically provides these traits for us Function, Function1, Function2 etc.
  val stringToIntConverter1: String => Int = new Function1[String, Int] {
    override def apply(s: String): Int = s.toInt
  }

  println(stringToIntConverter1("3") + 4)

  val stringToIntConverter2 = new ((String) => Int) {
    override def apply(s: String): Int = s.toInt
  }

  val adder1: ((Int, Int) => Int) = new Function2[Int, Int, Int] {
    override def apply(v1: Int, v2: Int): Int = v1 + v2
  }

  // Function types Function2[A,B,R] === (A,B) => R

  val adder2: ((Int, Int) => Int) = (v1: Int, v2: Int) => v1 + v2

  val adder3 = (v1: Int, v2: Int) => v1 + v2

  /* Exercises

  1) a function which takes 2 strings and concatenates them
     SEE: exercises/part3fp/WhatsAFunction.sc
  2) transform the MyPredicate and MyTransformer from exercises/part1oop/GenericList3.scala
     SEE: exercises/part3fp/GenericList4.scala
  3) Define a function which takes an int and returns another function which takes an int and returns and
     Int.
      - what is the type?
      - how to you implement it?
     SEE: exercises/part3fp/WhatsAFunction.sc
   */






}
