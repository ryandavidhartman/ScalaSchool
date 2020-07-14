package ScalaScheme

import scala.collection.mutable.ListBuffer

abstract class SchemeList {
  def head: Any
  def tail: Any

  def headSchemeList: SchemeList = head.asInstanceOf[SchemeList]
  def tailSchemeList: SchemeList = tail.asInstanceOf[SchemeList]

  def isEmpty: Boolean

  def map(f: Any => Any): SchemeList = this match {
      case SchemeNil => SchemeNil
      case SchemePair( head, tail: SchemeList ) => SchemePair( f(head), tail map f )
      case SchemePair( left, right ) => sys.error( s"SchemeList.map: improper list: ($left $right)" )
  }

  def foldLeft(zero: Any)(f: (Any, Any) => Any): Any = {

    @scala.annotation.tailrec
    def reducer(res: Any, tail: Any): Any = tail match {
      case SchemeNil => res
      case SchemePair(h, tail: SchemeList) => reducer( f(res, h), tail )
      case _ => sys.error( "SchemeList.fold: can't reduce an improper list" )
    }

    if(this == SchemeNil)
      zero
    else
      reducer( f(zero, head), tail )
  }

  def foldRight(zero: Any)(f: (Any, Any) => Any): Any = {
    if(this.isEmpty)
      zero
    else
      this.tailSchemeList.foldRight(f(this.head, zero))(f)
  }

  def length: Int = {
    def iter(xs: SchemeList, acc: Int): Int =
      if(xs.isEmpty)
        acc
      else iter(xs.tailSchemeList, acc+1)

    iter(this, 0)
  }

  def exists(p: Any => Boolean): Boolean = this match {
      case SchemeNil => false
      case SchemePair( head, tail: SchemeList ) =>
        if (p(head))
          true
        else
          tail exists p
      case SchemePair( left, right ) => sys.error(s"SchemeList.exists: improper list: ($left $right)" )
    }

  def forall(p: Any => Boolean): Boolean = this match {
    case SchemeNil => true
    case SchemePair( head, tail: SchemeList ) =>
      if (p(head))
        tail forall p
      else
        false
    case SchemePair( left, right ) => sys.error( s"SchemeList.forall: improper list: ($left $right)" )
  }

  def last: Any = this match {
    case SchemeNil => sys.error( s"SchemeList.last: empty list has no last element" )
    case SchemePair( elem, SchemeNil ) => elem
    case SchemePair( _, tail: SchemePair ) => tail.last
    case _ => sys.error( s"SchemeList.last: improper list" )
  }

  def isProperList: Boolean = this match {
    case SchemeNil => true
    case SchemePair( _, tail: SchemeList ) => tail.isProperList
    case _ => false
  }

  def toList: Either[(List[Any], Any), List[Any]] = SchemeList.toScalaList(this)

  def toProperList: Any = toList match {
    case Left(_) => sys.error( "SchemeList.toProperList: list is improper" )
    case Right(list) => list
  }
}

object SchemeList {
  def apply(elems: Any*): SchemeList = fromScalaSeq( elems )

  def unapplySeq(list: SchemeList): Option[Seq[Any]] = toScalaList(list) match {
    case Left(_) => None
    case Right(list) => Some(list)
  }

  def toScalaList(l: SchemeList): Either[(List[Any], Any), List[Any]] = {
    val buf = new ListBuffer[Any]

    @scala.annotation.tailrec
    def build(rest: SchemeList): Either[(List[Any], Any), List[Any]] = rest match {
      case SchemeNil => Right(buf.toList)
      case SchemePair(head, tail: SchemeList) =>
        buf += head
        build(tail)
      case SchemePair(left, right) =>
        buf += left
        Left(buf.toList, right)
    }

    build(l)
  }

  def fromScalaSeq(s: Seq[Any]): SchemeList  = {
    val it = s.reverseIterator

    @scala.annotation.tailrec
    def build(tail: SchemeList): SchemeList =
      if (it.hasNext)
        build(SchemePair(it.next, tail))
      else
        tail

    build(SchemeNil)
  }
}

case object SchemeNil extends SchemeList
{
  def head: Nothing  = sys.error("SchemeList: empty list has no head element")
  def tail: Nothing  = sys.error("SchemeList: empty list has no tail element")
  val isEmpty = true
  override def toString = "()"
}

case class SchemePair(var head: Any, var tail: Any) extends SchemeList
{
  val isEmpty = false

  override def toString:String = toList match {
    case Left((l, r)) => l.mkString("(", " ", "") + s" . $r)"
    case Right(l) => l.mkString("(", " ", ")")
  }
}
