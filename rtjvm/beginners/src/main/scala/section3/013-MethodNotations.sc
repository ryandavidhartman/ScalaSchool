import scala.language.postfixOps

class Person(val name: String, favoriteMovie: String, val age: Int = 18) {

  // Starting with this
  def likes(movie: String): Boolean = movie == favoriteMovie
  def hangOutWith(person: Person): String = s"${this.name} is hanging out with ${person.name}"
  def unary_! : String = s"$name, what the heck?!?"
  def isAlive: Boolean = true
  def apply(): String = s"Hi, my name is $name and I like $favoriteMovie"

  /*
  1)
  Overload the + operator, this an infix operator
  so mary + "the rockstart" => new person with name = original name + (the rockstar) and same favorite movie
  */
  def +(nickName: String): Person = new Person(s"$name ($nickName)", favoriteMovie)

  /*
  2)
  // Add an age to the Person class
  // Add a unary + operator that returns a new person with an incremented age, prefix operator
  */
  def unary_+ : Person = new Person(name, favoriteMovie, age+1)

  /*
  3)
  Add a "learns" method in the Person class => "Mary learns Scala"
  Add a learnScala method, calls learns method with "scala"
  Use it in postfix notation
   */
  def learns(subject: String): String = s"$name learns $subject"
  def learnsScala = learns("Scala")

  /*
  4) Overload the apply method
  marry.apply(2) => "Mary watched Inception 2 times"
   */

  def apply(times: Int): String = s"$name watched $favoriteMovie $times times"
}

val mary = new Person("Mary", "Inception")

//1
val newMary = mary + "The Rockstar"
newMary()

//2
val olderMary = +mary
olderMary.age

//3
println(mary.learns("Scala"))
println(mary learns("Scala"))
println(mary learnsScala)

//4
println(mary(4))

