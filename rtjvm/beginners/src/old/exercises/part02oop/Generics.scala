package old.lectures.exercises.part02oop

object Generics extends App {

  class MyList[A] {
      // use the type A
  }

  val listOfIntegers = new MyList[Int]
  val listOfStrings = new MyList[String]

  class MyMap[Key, Value]
  val myMap = new MyMap[Int, String]

  //generics
  object MyList {
    def empty[A]: MyList[A] = new MyList()
  }

  val emptyListOfIntegers = MyList.empty[Int]

  //variance problem
  class Animal
  class Cat extends Animal
  class Dog extends Animal

  // if Animal >: Cat implies MyList[Animal] >: MyList[Cat] then
  // MyList[T] is called COVARIANT, can we'd define that with class MyList[+A]


  // if Animal >: Cat implies MyList[Cat] >: MyList[Animal] then
  // MyList[T] is called CONTRAVARIANT, can we'd define that with class MyList[-A]

  // if Animal >: Cat implies nothing about the subtype relationship between
  // MyList[Animal] and MyList[Cat] then
  // MyList[T] is called INVARIANT, can we'd define that with class MyList[A]

  //More on Covariance
  abstract class CovariantList[+A] {
    def add[B >: A](element: B): CovariantList[B]
  }

  object CovariantEmpty extends CovariantList {
    override def add[A](a: A): CovariantList[A] = new CovariantCons[A](a, CovariantEmpty)
  }

  class CovariantCons[+A](h:A, t:CovariantList[A]) extends CovariantList[A] {
    override def add[B >: A](element: B): CovariantList[B] = new CovariantCons[B](element, this)
  }

  val animal: Animal = new Cat // this works since Cat :> Animal
  val animalList: CovariantList[Animal] = new CovariantCons[Cat](new Cat, CovariantEmpty)
  // should I be able to add a Dog to animalList???
  // ANSWER YES, but if you added a `Dog` to a list of Cats you'd get back a new list
  // of Animals i.e. the lowest common type

  //More on Invariance
  class InvariantList[A]
  // val invariantAnimalList: InvariantList[Animal] = new InvariantList[Cat]  //Won't work

  //More on Contravariance  --> It is WEIRD for Lists!!!
  class ContravariantList[-A]
  val contravariantList: ContravariantList[Cat] = new ContravariantList[Animal]
  // see how the subtype arrangement of the list is inverted compared to the subtypes of the
  // things in the list

  // Consider a different place where contravariance is more intuitive
  class Trainer[-A]
  val trainer: Trainer[Cat] = new Trainer[Animal]

  // bounded types
  // the cage class can be used for subtypes of animal
  class Cage[A <: Animal](animal: A)
  val cage = new Cage(new Dog)

  class Car
  //val newCage = new Cage(new Car)  //won't work
}
