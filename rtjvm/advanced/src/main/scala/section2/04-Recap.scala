package section2

import scala.annotation.tailrec
import scala.util.{Failure, Success, Try}

object Recap extends App {

  val aCondition: Boolean = false  // val assignment
  val aCondition2: Int = if (aCondition) 42 else 65 // if expression

  val aCodeBlock = {
    if(aCondition) 54 else 65
    101  // last val in a code bock is the value returned by the expression
  }

  // Unit type
  val theUnit: Unit = println("side effect ho!")

  // functions
  def aFunction(x:Int): Int = x + 1

  // recursion: stack and tail
  @tailrec
  def factorial(n: Int, acc: Int = 1): Int = if(n <= 1) acc else factorial(n-1, acc*n)

  // object-oriented programming
  class Animal
  class Dog extends Animal  // single class inheritance
  val aDog: Animal = new Dog  // polymorphism via subtyping

  // abstract data type
  trait Carnivore {
    def eat(a: Animal): Unit
  }

  // multiple inheritance with mix-ins
  class Crocodile extends Animal with Carnivore {
    override def eat(a: Animal): Unit = println(s"Yummy i ate a $a")
  }

  // method notation
  val aCroc = new Crocodile
  aCroc.eat(aDog)

  aCroc eat aDog  // infix notation to simulate natural languages

  // anonymous classes
  val aCarnivore = new Carnivore {
    override def eat(a: Animal): Unit = println("this is an anonymous class!")
  }

  def eatStuff(a: Carnivore, food: Animal): Unit = a.eat(food)
  eatStuff((a:Animal) => println("Single Method Anonymous Class SAM"), aDog)

  //generics
  abstract class MyList[+A]  // we'll talk about variance in THIS course

  // singletons and companions
  object MyList

  // case classes
  case class Person(name: String, age: Int)  // many built-in things over normal classes

  // exceptions and Try
  val aPotentialFailure: Try[Int] = Try {
    throw new Exception("error")
    101
  }

  aPotentialFailure match {
    case Success(value) => println(s"Yes! value: $value")
    case Failure(exception) => println(exception.getMessage)
  }

  // basically everything in Scala is an object! Functions are just instances of classes with
  // an apply method

  // functional programming
  val incrementerObject = new Function[Int, Int] {
    override def apply(v1: Int): Int = v1 + 1
  }

  incrementerObject(1) // returns 2

  val incrementLambda = (v1: Int) => v1 + 1

  incrementLambda(1) // returns 2 also!

  List(1,2,3).map(incrementLambda)  // functions are first class!

  // for-comprehensions
  val pairs: Seq[String] = for {
    num <- 1 to 3
    char <- List('a', 'b', 'c')
  } yield s"num:$num char:$char"

  // Scala collections: Seqs, Arrays, Lists, Vectors, Maps, Tuples
  val aMap = Map("Bob" -> 123, "Alice" -> 989)

  // Other "collections" Option, Try, Future  (map, flatMap, filter)

  // pattern matching
  val x = 2
  val order = x match {
    case 1 => "first"
    case 2 => "second"
    case 1 => "third"
  }

  val bob = Person("Bob", 22)
  val greeting = bob match {
    case Person(n, _) => s"Hi, my name is $n"
  }
}