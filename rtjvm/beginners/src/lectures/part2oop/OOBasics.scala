package lectures.part2oop

class Person(name: String, val age: Int) {
  // primary constructor body
  val x = 4
  println(1+3)

  def greet(name: String): Unit = println(s"${this.name} says: Hi $name")
  def greet(): Unit = println(s"Hi, I am $name")
}

object OOBasics extends App {

  val person = new Person("ryan", 18)
  println(person.age)
  println(person.x)

  person.greet("bob")
  person.greet()

}


