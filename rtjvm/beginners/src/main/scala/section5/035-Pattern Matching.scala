package section5

import scala.util.Random

object PatterMatching extends App {

  // ONE LIKE A SWITCH
  val random = new Random
  val x = random.nextInt(10)

  val description = x match {
    case 1 => "One"
    case 2 => "Two"
    case 3 => "Three"
    case _ => "More than three"  // _ here is the wildcard
  }

  println(s"x: $x description: $description")

  // TWO decomposition, or destructure

  case class Person(name: String, age: Int)
  val bob = Person("Bob", 20)

  val (name, age) = bob match {
    case Person(n, a) => (n, a) // here we decompose a case class into its class parameters!
    case _ => ("?", -1)
  }

  // you can also put guards (i.e. predicates) in the cases
  val (name2, age2) = bob match {
    case Person(n, a) if a > 21 => (n, a) // here we decompose a case class into its class parameters!
    case _ => ("too young", -1)
  }

  println(s"name: $name2")

  // THREE Notes on pattern match cases
  /*
    1. cases are run in order, first match wins
    2. if there is no match a scala.MatchError exception is thrown
    3. the return type is the most specific type of all the possible returns
   */


  // FOUR Pattern matching on sealed hierarchies
  sealed class Animal
  case class Dog(breed: String) extends Animal
  case class Parrot(greeting: String) extends Animal

  val animal: Animal =  Dog("short hair pointer")

  animal match {
    case Dog(breed) => println(s"""Matched a dog of the "$breed" bread""")
  }

}