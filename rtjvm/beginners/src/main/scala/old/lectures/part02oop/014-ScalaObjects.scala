package old.lectures.part02oop

object Objects extends App {

  // An object is a way to simulate class level functionality
  // Scala doesn't have the concept of static data

  object Person {  // This is of the Objects.Person.type  type and a single instance of this Type
    val N_EYES = 2
    def canFly: Boolean = false

    def from(mother:Person, father: Person): Person = new Person("bobbie")

    def apply(mother:Person, father: Person): Person = new Person("bobbie")
  }

  class Person(val name: String) {  // This is the Person type and provides instance level functionality

  }

  println(Person.N_EYES)
  println(Person.canFly)

  // Scala object is a SINGELTON Instance
  val mary = Person
  val john = Person
  val steve = new Person("steve")
  println(mary == john)
  println(mary == steve)

  val jan = new Person("jane")

  val bob = Person.from(jan, steve)
  val bob2 = Person(jan, steve)

  //Scala Application = Scala Object with a main function
  // def main(args: Array[String]): Unit

}
