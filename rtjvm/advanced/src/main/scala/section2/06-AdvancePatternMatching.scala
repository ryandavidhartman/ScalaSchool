package section2

import scala.util.Try
import scala.util.control.NonFatal

object AdvancedPatternMatching extends App {

  val numbers = List(1)
  val description = numbers match {
    case head :: Nil => println(s"the only element is $head")
  }

  /* The case statements can have:
      - constants
      - wildcards (_)
      - case classes
      - tuples
      - some special magic e.g. ( x :: Nil)
   */

  // Making your own classes available for pattern matching
  // Remember case classes give you this for free

  class Person(val name: String, val age: Int)

  // First make a companion object for Person

  object Person {
    // Now we can de-structure instances of the Person class!
    def unapply(person: Person): Option[(String, Int)] = Some(person.name, person.age)
  }

  val bob = new Person(name = "Bob", age = 34)

  val greeting = bob match {
    case Person(n, a) => s"Hi $n you are $a years old!"
  }
  println(greeting)

  object LegalPerson {
    def unapply(p: Person): Option[(String, Int)] = if(p.age > 21) Some((p.name, p.age)) else None

    def unapply(age: Int): Option[String] = Some(if(age > 21) "Legal" else "Minor")
  }

  val legalPersonGreeting = bob match {
    case LegalPerson(n, a) => s"We have a legal person named $n who is $a years old"
  }

  println(legalPersonGreeting)


  Try {
    new Person("Youngster", 8) match {
      case LegalPerson(n, a) => s"We have a legal person named $n who is $a years old"
    }
  }.recover {
    case NonFatal(e) => println(e)
  }

  val status = 19 match {
    case LegalPerson(s: String) => s
  }


}