package utilities

import scala.util.Try

object SchemeUtilities {

  type SchemeData = Any
  type Pair = (SchemeData, SchemeData)

  def cons(left: SchemeData, right: SchemeData): Pair = (left, right)

  def car(arg: SchemeData): SchemeData = arg match {
    case x: Pair => x._1
    case xs: List[SchemeData] => xs.head
    case _ => throw new IllegalArgumentException(s"car of arg: $arg")
  }

  def cdr(arg: SchemeData): SchemeData = arg match {
    case x: Pair => x._2
    case xs: List[SchemeData] => xs.tail
    case _ => throw new IllegalArgumentException(s"cdr of arg: $arg")
  }

  def isPair(arg: SchemeData): Boolean = arg match {
    case _:Pair => true
    case _ => false
  }

  def isNull(arg: SchemeData): Boolean = (arg != null) && (arg != List.empty)
  def isDefined(arg: SchemeData): Boolean = !isNull(arg)

  def multiply(args: SchemeData*): Double = {
    @scala.annotation.tailrec
    def mHelper[T](acc: Double, rest:Seq[Double]):Double =
      if(rest.isEmpty)
        acc
      else
        mHelper(acc * rest.head, rest.tail)
    mHelper(acc=1.0, args.map(_.toString.toDouble))
  }

  def sum(args: SchemeData*): Double = {
    @scala.annotation.tailrec
    def mHelper[T](acc: Double, rest:Seq[Double]):Double =
      if(rest.isEmpty)
        acc
      else
        mHelper(acc + rest.head, rest.tail)
    mHelper(acc=0.0, args.map(_.toString.toDouble))
  }

  def division(args: SchemeData*): Double = {
    if(args.length == 1)  1.0/ multiply(args)
    else if(args.length > 1) args.head.toString.toDouble / multiply(args.tail:_*)
    else throw new IllegalArgumentException("Division needs at least one number")
  }
}
