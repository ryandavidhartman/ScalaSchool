package section2

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
    // Now we can destructure instances of the Person class!
    def unapply(person: Person): Option[(String, Int)] = Some(person.name, person.age)
  }

  val bob = new Person(name = "Bob", age = 34)

  val (name: String, age: Int) = bob match {
    case Person(n, a) => (n, a)
  }



}