import scala.util.{Failure, Success, Try}

sealed abstract class Expression

case class Number(value: Double) extends Expression {
  override def toString: String = if(value%1.0 == 0.0) value.toInt.toString else value.toString
}

case class Variable(name: String) extends Expression {
  override def toString: String = name
}

case class Sum(addend: Expression, augend: Expression) extends Expression {
  override def toString: String = (addend, augend) match {
    case (Number(0.0), a2) => a2.toString
    case (a1, Number(0.0)) => a1.toString
    case (Number(a), Number(b)) => Number(a + b).toString
    case _ => s"[$addend+${augend}]"
  }
}

case class Product(multiplier: Expression, multiplicand: Expression) extends Expression {

  override def toString: String = (multiplier: Expression, multiplicand) match {
    case (Number(0.0), _) | (_, Number(0.0)) => "0"
    case (a, Number(1.0)) => a.toString
    case (Number(1.0), a) => a.toString
    case (Number(n), Number(m)) => Number(n * m).toString
    case _ => s"$multiplier*$multiplicand"
  }

}

case class Exponentiation(base: Expression, exponent: Number) extends Expression {
  override def toString: String =  (base,exponent) match {
    case (_, Number(0.0)) => "0"
    case (a, Number(1.0)) => a.toString
    case _ => s"$base^$exponent"
  }
}

def makeSum(a1: Expression, a2: Expression): Expression = (a1, a2) match {
  case (Number(0.0), a2) => a2
  case (a1, Number(0.0)) => a1
  case (Number(a), Number(b)) => Number(a + b)
  case (a1, a2) => Sum(a1, a2)
}

def makeProduct(m1: Expression, m2: Expression): Expression = (m1, m2) match {
  case (Number(0.0), _) | (_, Number(0.0)) => Number(0.0)
  case (a, Number(1.0)) => a
  case (Number(1.0), a) => a
  case (Number(n), Number(m)) => Number(n * m)
  case (m1, m2) => Product(m1, m2)
}

def makeExponentiation(b: Expression, e: Number): Expression = (b, e) match {
  case (_, Number(0.0)) => Number(0.0)
  case (a, Number(1.0)) => a
  case (b, e) => Exponentiation(b, e)
}


def deriv(expression: Expression, variable: Variable): Expression =
  expression match {
    case Number(_) => Number(0)
    case v: Variable => Number(
      if (v == variable) 1
      else 0
    )
    case Sum(a1, a2) =>
      makeSum(deriv(a1, variable), deriv(a2, variable))
    case Product(m1, m2) => makeSum(
      makeProduct(deriv(m2, variable), m1),
      makeProduct(deriv(m1, variable), m2)
    )
    case Exponentiation(b, e) =>
      makeProduct(e, makeExponentiation(b, Number(e.value - 1.0)))

    case exp => throw new Exception(s"Unknown expression type: ${exp}")
  }

def makeNumber(str: String): Number = Number(str.trim.toDouble)

def isNumber(x: String): Boolean =
  Try {
    x.toDouble
  } match {
    case Success(_) => true
    case Failure(_) => false
  }

implicit def makeExpression(str: String): Expression = str.trim match {
  case s"$left+$right" => makeSum(makeExpression(left), makeExpression(right))
  case s"$left*$right" => makeProduct(makeExpression(left), makeExpression(right))
  case s"$left^$right" => makeExponentiation(makeExpression(left), makeNumber(right))
  case s"($e)" => makeExpression(e)
  case n if isNumber(n) => makeNumber(n)
  case x => Variable(x)
}

deriv("x + y", Variable("x"))
deriv("x^2", Variable("x"))
deriv("3*x^3" , Variable("x"))
//deriv("3*(x^2 + 5*x)" , Variable("x"))
makeExpression("3*(x^2 + 5*x + 2)")





