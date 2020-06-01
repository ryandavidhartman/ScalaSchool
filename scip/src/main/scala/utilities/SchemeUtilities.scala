package utilities

import scala.util.Try

object SchemeUtilities {

  type SchemeData = Any
  type Pair = (SchemeData, SchemeData)

  def cons(args: SchemeData*): Pair = args match {
    case Seq(l, r) => (l,r)
    case _ => throw new IllegalArgumentException(s"Cons requires 2 parameters.  args={$args} is invalid")
  }

  def car(arg: SchemeData): SchemeData = arg match {
    case x: Pair => x._1
    case xs: List[SchemeData] => xs.head
  }

  def cdr(arg: SchemeData): SchemeData = arg match {
    case x: Pair => x._2
    case xs: List[SchemeData] => xs.tail
  }

  def isPair(arg: SchemeData): Boolean = arg match {
    case _:Pair => true
    case _ => false
  }

  def isDefined(arg: SchemeData): Boolean = Try{arg.toString}.isSuccess
  def isNull(arg: SchemeData): Boolean = !isDefined(arg)

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
