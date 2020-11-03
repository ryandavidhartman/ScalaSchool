package section3

object ScalaObjects extends App {

  // An object is a way to simulate class level functionality, without needing an instance
  // of a class.  Scala doesn't have the concept of static data!
  // But Scala object provides something like static data

  // objects can have fields and methods, but no parameters!
  // This is a singleton instance.  We define and type and the only instance of this type
  object Person { // This is of the Objects.Person.type  type and a single instance of this Type

    val N_EYES = 2 // compare to a public static final int in a Java class
    def canFly: Boolean = false

    // here is a factory method
    def from(mother: Person, father: Person): Person = new Person("bobbie")

    // here is the "default" factory method
    def apply(mother: Person, father: Person): Person = new Person("bobbie")
  }

  class Person(val name: String) { // This is the Person type and provides instance level functionality

  }


  // class Person and object Person are "companions"
  // Instance data is in the class Person, class level data in the object

  println(Person.N_EYES)
  println(Person.canFly)

  // Scala object is a SINGELTON Instance
  val person1 = Person
  val person2 = Person
  println(person1 == person2)

  val steve1 = new Person("steve")
  val steve2 = new Person("steve")
  println(steve1 == steve2)

  val jan = new Person("jane")

  val bob: Person = Person.from(jan, steve1)
  val bob2: Person = Person(jan, steve1) //calls the apply in the Person Object

  //Scala Application = Scala Object with a main function
  // def main(args: Array[String]): Unit

  import scala.math._

  class Circle(radius: Double) {
    import Circle._
    def area: Double = calculateArea(radius)
  }

  object Circle {
    private def calculateArea(radius: Double): Double = Pi * pow(radius, 2.0)
  }

  val circle1 = new Circle(5.0)

  print(circle1.area)

}
