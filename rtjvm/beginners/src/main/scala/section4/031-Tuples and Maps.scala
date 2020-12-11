package section4

import java.util.NoSuchElementException
import scala.collection.MapView
import scala.util.Try

object TuplesAndMaps extends App {

  // Tuples == finite ordered "lists"
  val aTuple1 = Tuple2(2, "hello, scala") // Tuple2[Int, String] == (Int, String)
  val aTuple2 = (2, "hello, scala")

  println(s"aTuple1 1 is:${aTuple1._1} and aTuple1 2 is:${aTuple1._2}")
  println(aTuple1.copy(_2 = "goodbye Java"))
  println(aTuple1.swap)

  // Maps == keys -> values
  val aMap: Map[String, Int] = Map()
  val phoneBook = Map(("Jim", 555), ("Daniel", 789)) // Map("Jim" -> 555, "Daniel" -> 789))

  println(phoneBook)

  // Operations on maps
  println(phoneBook.contains("Jim"))
  println(phoneBook("Jim"))

  val testPhoneBook = Try {
    phoneBook("Mary")
  }
  testPhoneBook.recover {
    case _: NoSuchElementException =>
      println("Whoops phonebook doesn't have that key!")
      Try("no number found!")
  }

  val phoneBook2 = Map("Jim" -> 555, "Daniel" -> 789).withDefaultValue(-1)
  val marysNumber = phoneBook2("Mary")
  assert(marysNumber == -1)

  // How to we add something to a Map?
  val phoneBook3: Map[String, Int] = phoneBook2 + ("Mary" -> 678)
  println(phoneBook3)

  // functionals on Maps.  map, flatMap, and filter all exist
  println(phoneBook3.map(p => p._1.toUpperCase -> p._2))

  // filter Keys
  println(phoneBook3.view.filterKeys(k => k != "Jim").toMap)

  // map values
  println(phoneBook3.view.mapValues(_ * 10).toMap)

  // conversions
  println(phoneBook3.toList)

  val list = List("key1" -> 1, "key2" -> 2)
  println(s"List: $list Map: ${list.toMap}")

  // groupBy
  val names = List("Bob", "James", "Angela", "Mary", "Daniel", "Jim", "Barry")
  println(names.groupBy(name => name.charAt(0)))

}
