package section3


// Note without the var, class parameters are
// not class fields!  (But this isn't so for case classes)
class Person(name: String, val age: Int) {
  // primary constructor body.  This is run on every class instantiation
  val x = 2  //this is a field
  println(1+3)

  def greet(name: String): Unit = println(s"${this.name} says: Hi $name")
  def greet(): Unit = println(s"Hi, I am $name")

  // Here is a secondary constructor
  def this(name: String) = this(name, 0)  // probably easier to just use a default
  // parameter.

  //unlike normal code blocks the "last" expression isn't returned here
}

object OOBasics extends App {

  val person = new Person("John", 26)
  // again since `name` isn't a field we can't access from outside the class
  println(person.age)
  println(person.x)

  person.greet("bob")
  person.greet()

}


