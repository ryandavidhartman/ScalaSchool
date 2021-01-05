package section5

import section4.GenericList6.{Cons6, Empty6, MyList6}

object AllThePatterns extends App {

  // 1 - literals, you can match on specific literal values
  val x: Any = "Scala"
  val literals = x match {
    case 1 => "a number literal"
    case "Scala" => "a string literal"
    case true => "a boolean literal"
  }

  println(s"Yay matching on literals works.  $literals was found")

  // 2 - match anything, VIA wildcard (_)

  val matchAnythingWildcard = x match {
    case _ => "this will always match"
  }

  // 2 - match anything, VIA  variable
  val matchAnythingVariable = x match {
    case y => "this will always match, and we get the value" + y
  }

  // 3 - pattern matching on tuples
  val aTuple  = (1,2)
  val matchATuple = aTuple match {
    case (1,1) => ()
    case(something, 2) => "I've found " + something
  }

  println(matchATuple)

  val nestedTuple = (1, (2,3))
  val matchANestedTuple = nestedTuple match {
    case (_, (2, v)) => "inner tuple ends with " + v
  }

  // 4 - case classes - constructor pattern
  val aList: MyList6[Int] = Cons6(1, Cons6(2, Empty6))  // any case class will do here
  val matchAList = aList match {
    case Empty6 => ()
    case Cons6(head, Cons6(subhead, subtail)) => s"head: $head, subhead $subhead, subtail: $subtail"
  }

  println(s"Pattern matching on aList gives $matchAList")

  // 5 - patterns on the standard Scala List type
  val aStandardList = List(1,2,3,42)
  val aStandardListMatch = aStandardList match {
    case List(1, _, _, _) => "starts with a one and has 4 elements"   // List has an extractor we can use
    case List(1, _*) => "starts with one, and has n elements"
    case 1 :: List(_) => "another way to match an arbitrary list starting with a 1"  // infix pattern
    case List(1,2,3) :+ 42 => "mix and match extractors and infix!"
  }

  // 6 - patterns with type specifiers
  val unknown: Any = 2
  val unknownMatch = unknown match {
    case _: List[Int] => "we got a list!"
    case _: Int => "we got an int!"
  }

  // 7 - name biding pattern matches
  val nameBindingMatch= aList match {
    case nonEmptyList @ Cons6(_, _) => nonEmptyList
    case Cons6(1, rest @ Cons6(2, _)) => rest //matches a list starting with 1,2 using name bindins (@)
  }

  println(nameBindingMatch)

  // 8 - multiple pattern matches
  val multiPattern = aList match {
    case Empty6 | Cons6(0, _) => "this has 2 matches in on case!"
    case _ => "backup"
  }

  // 9 - if guards in patterns
  val secondElementSpecial = aList match {
    case Cons6(_, Cons6(specialElement, _)) if specialElement % 2 == 0 => specialElement
  }
}