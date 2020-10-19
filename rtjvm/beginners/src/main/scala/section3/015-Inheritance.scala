package section3

object Inheritance extends App {

  // Scala has single class inheritance, but allows mix-ins

  // public, private and protected are the access modifiers
  class Animal {
    val creatureType = "wild"
    protected def eat(): Unit = println("Animal (Base) Class eat: nomnomnom")
    // protected for use in this class and subclasses
  }

  // Class cat is a subclass of Animal
  // gets public & protected methods and fields
  class Cat extends Animal {
    def crunch(): Unit = {
      eat()
      println("Cat Class: Crunch")
    }
  }

  val cat = new Cat
  // we can't directly call the protected method eat() from the Animal super class.
  // But we can access it from a method in the Cat subclass
  cat.crunch()

  // constructors
  class Person(name: String, age: Int) {
    def this(name: String) = this(name, 0)  // auxiliary  constructor
  }

  //need to call the super class's  constructor
  class Adult(name: String, age: Int, idCard: String) extends Person(name, age)
  class Adult2(name: String, idCard: String) extends Person(name)  //you can call the supertype's secondary
  // constructor too

  //overriding, you can override methods, vals and vars in class parameters or
  //in the constructor body
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
  println(s"unknown creature type: ${unknownAnimal.creatureType}")


  // the super keyword -> lets you call a baseclass implementation
  // method from a subclass


  /* preventing overrides
  1 - use final on a method
  2 - use final on a class definition
  3 - use sealed -> can only be extended in the same file.

*/

}
