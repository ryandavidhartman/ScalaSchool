package lectures.part2oop

object Inheritance extends App {

  // public, private and protected are the access modifiers
  class Animal {
    val creatureType = "wild"
    protected def eat(): Unit = println("Base Class eat")
  }

  // Class cat is a subclass of Animal
  // gets public & protected methods and fields
  class Cat extends Animal {
    def crunch(): Unit = {
      eat()
      println("Crunch")
    }
  }

  val cat = new Cat
  // we can't directly call the protected method eat() from the Animal super class.
  // But we can access it from a method in the Cat subclass
  cat.crunch()

  // constructors
  class Person(name: String, age: Int)

  //need to call the superclass's constructor
  class Adult(name: String, age: Int, idCard: String) extends Person(name, age)

  //overriding, you can override methods, vals and vars
  class Dog(override val creatureType: String) extends  Animal {
    // fields can be overridden here or in the constructor
    //override val creatureType: String = "Domestic"

    override def eat(): Unit = {
      super.eat()
      println("Dog class eats")
    }
  }

  val d = new Dog("Domestic")
  d.eat()
  println(d.creatureType)


  // type substitution (polymorphism)
  // a method call will go to the most "overridden method" available
  val unknownAnimal: Animal = new Dog("K9")


  // the super keyword -> lets you call baseclass an implementation
  // method from a subclass


  /* preventing overrides
  1 - use final on a method
  2 - use final on a class definition
  3 - use sealed -> can only be extended in the same file.

*/

}
