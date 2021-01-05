/*
Question consider the following pattern match below
*/

val numbers = List(1,2,3)
val numbersMatch = numbers match {
  case listOfStrings: List[String] => "a list of strings: " + listOfStrings.mkString(",")
  case listOfNumbers: List[Int] => "a list of numbers: " + listOfNumbers.mkString(",")
}

// Do we get the list of strings and not the list of numbers?


// ANSWER: JVM type erasure