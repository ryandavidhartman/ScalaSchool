package lectures.part2oop

object AbstractDataTypes extends App {

  // abstract class members are undefined
  abstract class Animal {
    val creatureType: String
    def eat(): Unit
  }

  // val animal = new Animal won't work, abstract classes can't be instantiated

  class Dog extends Animal {
    override val creatureType: String = "Canine"  // you could leave off the override

    override def eat(): Unit = println("crunch crunch") // you could leave off the override
  }

  // Traits
  // 1 - they can not have constructor parameters
  // 2 - You can extend only one class but you can mixin multiple traits
  // 3 - Traits describe a behavior

  trait Carnivore {
    def eat(animal: Animal): Unit
    val preferredMeal: String = "fresh meal"
  }

  trait ColdBlooded

  class Crocodile extends Animal with Carnivore with ColdBlooded {
    override val creatureType: String = "croq"

    override def eat(): Unit = println("nomnomnom")

    override def eat(animal: Animal): Unit = println(s"I'm a croq and I'm eating ${animal.creatureType}")
  }

  val dog = new Dog
  val croc = new Crocodile
  croc.eat(dog)
  println(croc.preferredMeal)


}
