package calculator

sealed abstract class Expr
final case class Literal(v: Double) extends Expr
final case class Ref(name: String) extends Expr
final case class Plus(a: Expr, b: Expr) extends Expr
final case class Minus(a: Expr, b: Expr) extends Expr
final case class Times(a: Expr, b: Expr) extends Expr
final case class Divide(a: Expr, b: Expr) extends Expr

object Calculator {
  def computeValues(
      namedExpressions: Map[String, Signal[Expr]]): Map[String, Signal[Double]] = {
    for {
      (name, signalExpr) <- namedExpressions
    } yield (name, Signal(eval(signalExpr(), namedExpressions - name)))

  }

  def eval(expr: Expr, references: Map[String, Signal[Expr]]): Double = expr match {
    case Literal(x) => x
    case Plus(a1, b1) => eval(a1, references) + eval(b1, references)
    case Minus(a2, b2) => eval(a2, references) - eval(b2, references)
    case Times(a3, b3) => eval(a3, references) * eval(b3, references)
    case Divide(a4, b4) => {
      val denominator = eval(b4, references)
      if(denominator != 0) eval(a4, references) / denominator else Double.NaN
    }
    case Ref(name) => eval(getReferenceExpr(name, references), references)
  }

  /** Get the Expr for a referenced variables.
   *  If the variable is not known, returns a literal NaN.
   */
  private def getReferenceExpr(name: String, references: Map[String, Signal[Expr]]): Expr = {
    references.get(name).fold[Expr] {
      Literal(Double.NaN)
    } { exprSignal =>
      exprSignal()
    }
  }
}
