package section2

import scala.annotation.tailrec

object MorePatternMatching extends App {

  ///////////////////////////////////////////////////////
  // infix patterns  here below is an infix pattern:
  ///////////////////////////////////////////////////////
  val numbers = List(1)
  val description = numbers match {
    case head :: Nil => println(s"the only element is $head")  // infix patten
  }

  // case classes can do infix style pattern matches when their constructors only
  // takes 2 parameters

  case class MyEither[A, B](a: A, b: B)  // This is own fake Either class

  val either = MyEither(2, "two")

  val useInfixPattern = either match {
    case number MyEither string => s"$number or $string"  // same as case MyEither(number, string)!
  }

  println(useInfixPattern)

  ///////////////////////////////////////////////////////
  // decomposing sequences
  ///////////////////////////////////////////////////////

  val varArgPattern = numbers match {
    case List(1, _*) => "vararg pattern match!"  // case 1 :: _ would also work
    case _ => "oops!"
  }

  println(varArgPattern)

  // What do you need to make var arg pattern matching work in your classes?
  // you need an unapplySeq method!

  abstract class MyList[+T] {
    def head(): T
    def tail: MyList[T]
  }

  // Say we have the standard list implementation

  case object EmtpyList extends MyList[Nothing] {
    override def head(): Nothing = throw new UnsupportedOperationException("head of empty list")
    override def tail: MyList[Nothing] = throw new UnsupportedOperationException("tail of empty list")
  }

  case class MyCons[+T](override val head: T, override val tail: MyList[T]) extends MyList[T]

  //ok to support var arg pattern matches here is the type of unapply method we need:

  object MyList {
    // add an unapplySeq so we can do var arg pattern matching!
    def unapplySeq[T](list: MyList[T]): Option[Seq[T]] = {
      if (list == EmtpyList)
        Some(Seq.empty[T])
      else
        unapplySeq(list.tail).map(list.head +: _)
    }
  }

  val numbers2: MyList[Int] = MyCons(1, MyCons(2, MyCons(3, EmtpyList)))

  val varArgPattern2 = numbers2 match {
    case MyList(1, 2, _*) => "vararg pattern match on custom class!"
    case _ => "oops didn't work!"
  }

  println(varArgPattern2)

}