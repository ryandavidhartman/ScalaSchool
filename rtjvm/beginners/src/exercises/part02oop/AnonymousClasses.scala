package exercises.part02oop

import scala.runtime.java8.JFunction0$mcB$sp

object AnonymousClasses extends App {

  abstract class Animal {
    def eat(): Unit
  }

  val funnyAnimal1: Animal = new Animal {
    override def eat(): Unit = println("funny1")
  }

  class Anon2 extends Animal {
    override def eat(): Unit = println("funny2")
  }

  val funnyAnimal2: Animal = new Anon2

  val funnyAnimal3: Animal = () => println("funny3")

  val funnyFunction1:Function0[Unit] = () => println("funny4")

  val funnyFunction2:Function0[Unit] = new Function0[Unit] {
    override def apply(): Unit = println("funny5")
  }

  println("1: " + funnyAnimal1.getClass)
  println("2: " + funnyAnimal2.getClass)
  println("3: " + funnyAnimal3.getClass)
  println("function1: " + funnyFunction1.getClass)
  println("function2: " + funnyFunction2.getClass)

  class Person(name: String) {
    def sayHi(): Unit = println(s"Hi, my name is $name, how can I help you?")
  }

  val jim = new Person("Jim") {
    override def sayHi(): Unit = println("Jim is the man")
  }

  println("What type is jim? " + jim.getClass)
  println("Is jim a person? " + jim.isInstanceOf[Person])

}
