package section5

object PatternsEverywhere extends App {
  // big idea #1  => Pattern matches in the try/catch

  try {
    // code
  } catch {
    case e: RuntimeException => "runtime"
    case npe: NullPointerException => "npe"
    case _ => "other exception"
  }

  /* So we see the catch block is really just a pattern match!  something like:
    try {
    // code
    } catch(e) {
      e match {
        case e: RuntimeException => "runtime"
        case npe: NullPointerException => "npe"
        case _ => "other exception"
      }
    }
  */

  // big idea #2
  val list = List(1,2,3,4)

  val evenOnes = for {
    x <- list if x % 2 == 0
  } 10 * x

  /* It turns our our generators (here filters) are also based on pattern matching!
  */

  //here is a more explicit example
  val tupleList = List((1,2), (3,4))
  val tuplesAdded = for {
    (first, second) <- tupleList  // here is a case class extractor
  } yield (first+second)

  // big idea #3
  // multiple value assignment via name binding is possible via pattern matching
  val tuple = (1,2,3)
  val (a, b, c) = tuple  // pattern matching and name binding is available!

  val head :: tail = list  // again multiple variable assignment in action!

  // big idea #4
  // Partial functions literals can be constructed via pattern matching!

  val mappedList = list.map {
    case v if v %2 == 0 => v  + " is even"
    case 1 => "the number 1"
    case _ => "something else"
  }

  // the above is sugar for:

  val mappedList2 = list.map {_ match {
      case v if v % 2 == 0 => v + " is even"
      case 1 => "the number 1"
      case _ => "something else"
    }
  }


}
