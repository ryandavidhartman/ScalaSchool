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
    for ( (name, sig) <- namedExpressions) yield (name,Signal(eval(sig(), namedExpressions)))
  }

  def isCircular(name: String, expr: Expr, references: Map[String, Signal[Expr]], nameMap: Map[String, Int]) : Boolean= {
    if(nameMap.isDefinedAt(name)) {
      true
    } else {
        expr match {
          case Literal(v) =>
            println("ISC Literal: " + v)
            false
          case Ref(name2) =>
            println("ISC Ref: " + name2)
            val nameMap2 = nameMap.+((name2, 1))
            //val exprSigOpt = references.get(name2)
            val expr2 = getReferenceExpr(name2, references)
            isCircular(name, expr2/*(exprSigOpt.get)()*/, references, nameMap2)
          case Plus(a, b) =>
            println("ISC Plus: " + a + ", " + b)
            isCircular(name, a, references, nameMap) | isCircular(name, b, references, nameMap)
          case Minus(a, b) =>
            println("ISC Minus: " + a + ", " + b)
            isCircular(name, a, references, nameMap) | isCircular(name, b, references, nameMap)
          case Times(a, b) =>
            println("ISC Times: " + a + ", " + b)
            isCircular(name, a, references, nameMap) | isCircular(name, b, references, nameMap)
          case Divide(a, b) =>
            println("ISC Divide: " + a + ", " + b)
            isCircular(name, a, references, nameMap) | isCircular(name, b, references, nameMap)

        }
    }
  }

  def eval(expr: Expr, references: Map[String, Signal[Expr]]): Double = {
    expr match {
      case Literal(v) =>
        println("EVAL Literal: " + v)
        v
      case Ref(name) =>
        println("EVAL Ref: " + name)
        if(isCircular(name, getReferenceExpr(name, references), references, Map[String, Int]())) {
          Double.NaN
        } else {
          eval(getReferenceExpr(name, references), references)
        }
      case Plus(a, b) =>
        println("EVAL Plus: " + a + ", " + b)
        eval(a, references) + eval(b, references)
      case Minus(a, b) =>
        println("EVAL Minus: " + a + ", " + b)
        eval(a, references) - eval(b, references)
      case Times(a, b) =>
        println("EVAL Times: " + a + ", " + b)
        eval(a, references) * eval(b, references)
      case Divide(a, b) =>
        println("EVAL Divide: " + a + ", " + b)
        eval(a, references) / eval(b, references)
    }
  }

  /** Get the Expr for a referenced variables.
   *  If the variable is not known, returns a literal NaN.
   */
  private def getReferenceExpr(name: String,
      references: Map[String, Signal[Expr]]) = {
    references.get(name).fold[Expr] {
      Literal(Double.NaN)
    } { exprSignal =>
      exprSignal()
    }
  }

}
