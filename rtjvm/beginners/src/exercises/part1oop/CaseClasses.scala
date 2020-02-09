package exercises.part1oop

object CaseClasses extends App {

  /*
  case classes come with an equals, hashCode, toString and companion object
   */

  case class Person(name: String, age: Int)

  // 1. class parameters are fields
  val jim = new Person("Jim", 34)
  println(jim.name)

  // 2. sensible toString
  // Note println(jim) == println(jim.toString)
  println(jim)

  //3. equals and hashCode are implemented out of the box
  val jim2 = Person("Jim", 34)
  println(jim == jim2)

  //4. case classes have handy copy methods
  val jim3 = jim.copy(age=35)
  println(jim3)

  //5. case classes have companion objects
  val thePerson = Person
  val mary = thePerson("mary", 45)  // this is == to thePerson.apply("mary", 45) == Person.apply("mary", 45)


}
