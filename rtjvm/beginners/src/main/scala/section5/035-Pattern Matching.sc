/*
Exercises
 */

trait Expr
case class Number(n: Int) extends Expr
case class Sum(e1: Expr, e2: Expr) extends Expr
case class Prod(e1: Expr, e2: Expr) extends Expr

// Use pattern matching to make a function that
// parses the above class hierarchy into a human
// readable math expression
// e.g.  Sum(Number(2), Number(3))  => 2 + 3
// e.g.  Sum(Sum(Number(2), Number(3)), Number(4))  => 2 + 3 + 4
// e.g.  Prod(Number(2), Number(3))  => 2 * 3
// e.g.  Sum(Prod(Number(2), Number(3)), Number(4))  => 2 * 3 + 4
// e.g.  Prod(Sum(Number(2), Number(3)), Number(4))  => (2 + 3) + 4

def parse(e: Expr): String = e match {
  case Number(n) => n.toString
  case Sum(e1, e2) => s"${parse(e1)} + ${parse(e2)}"
  case Prod(e1, e2) => {
    val leftParen = if(e1.isInstanceOf[Sum]) ("(", ")") else ("", "")
    val rightParen = if(e2.isInstanceOf[Sum]) ("(", ")") else ("", "")
    s"${leftParen._1}${parse(e1)}${leftParen._2} * ${rightParen._1}${parse(e2)}${rightParen._2}"
  }
}

parse(Sum(Number(2), Number(3)))
parse(Sum(Sum(Number(2), Number(3)), Number(4)))
parse(Prod(Number(2), Number(3)) )
parse(Sum(Prod(Number(2), Number(3)), Number(4)))
parse(Prod(Sum(Number(2), Number(3)), Number(4)))
parse(Prod(Number(2), Sum(Number(3), Number(4))))


