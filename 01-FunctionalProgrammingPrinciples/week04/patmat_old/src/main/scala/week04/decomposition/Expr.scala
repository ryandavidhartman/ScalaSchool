package week04.decomposition

trait Expr {
  def eval: Int
}

case class Number(n: Int) extends Expr {
  def eval: Int = n

}

/**
  * new Sum(e1, e2) represents e1 + e2
  */
case class Sum(e1: Expr, e2: Expr) extends Expr {
  def eval: Int = e1.eval + e2.eval
}

/**
  * We could do something similiar for prod and variable.
  *
  * This technique of OO decompostion works well.  But we want to add a Show method
  * we need to touch each class.
  *
  * Also to do a simplification of say a*b + a*c -> a * (b+c) we can't implement this simplification
  * without adding type checks/or accessors to the classes
  */



