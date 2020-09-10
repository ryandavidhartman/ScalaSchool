import ScalaScheme.Primitives.{SD, SL, caddr, cadr, car, cddr, cdr, cdrL, isPair, length, schemeDataToSchemeList}
import ScalaScheme.SchemeList
import ScalaScheme.SchemeMath.{multiply, sum}

import scala.util.{Failure, Success, Try}

def isVariable(x: SD): Boolean = x.isInstanceOf[Symbol]

def isSameVariable(v1: SD,  v2: SD): Boolean =
  isVariable(v1) && isVariable(v2) && v1.equals(v2)

def isNumber(x: SD): Boolean =
  Try {
    x.toString.toDouble
  } match {
    case Success(_) => true
    case Failure(_) => false
  }

def isEqualNum(x:SD, y:SD): Boolean = isNumber(x) && x == y

def makeSum(a1:SD, a2:SD): SD =
  if(isEqualNum(a1, 0)) a2
  else if(isEqualNum(a2, 0)) a1
  else if(isNumber(a1) && isNumber(a2)) sum(a1,a2)
  else SchemeList(Symbol("+"), a1, a2)

def makeSums(as: SL): SD =
  if(as.length == 2)
    makeSum(car(as), cadr(as))
  else
    makeSum(car(as), makeSums(cdrL(as)))

def makeProduct(m1: SD,m2: SD): SD =
  if(isEqualNum(m1, 0) || isEqualNum(m2, 0)) 0
  else if(isEqualNum(m1, 1)) m2
  else if(isEqualNum(m2, 1)) m1
  else if(isNumber(m1) && isNumber(m2)) multiply(m1,m2)
  else SchemeList(Symbol("*"), m1, m2)

def makeProducts(as: SL): SD =
  if(as.length == 2)
    makeProduct(car(as), cadr(as))
  else
    makeProduct(car(as), makeProducts(cdrL(as)))

def makeExponentiation(b: SD, e: SD): SD = {
  if(isEqualNum(e, 0)) 1
  else if(isEqualNum(e, 1)) b
  else if(isEqualNum(b, 0)) 0
  else SchemeList(Symbol("**"), b, e)
}


def isSum(x: SD): Boolean = isPair(x) && (car(x) == Symbol("+"))

def isProduct(x: SD): Boolean = isPair(x) && (car(x) == Symbol("*"))

def isExponentiation(x: SD): Boolean = isPair(x) && (car(x) == Symbol("**"))

def addend(s: SD): SD = if(isSum(s)) cadr(s) else throw new IllegalArgumentException("not a sum")

def augend(s: SD): SD = {
  if (!isSum(s)) throw new IllegalArgumentException("not a sum")
  val terms = cddr(s)
  if (length(terms) == 1)
    car(terms)
  else
    makeSums(terms)
}

def multiplier(p: SD): SD = if(isProduct(p)) cadr(p) else throw new IllegalArgumentException("not a product")

def multiplcand(p: SD): SD = {
  if(!isProduct(p)) throw new IllegalArgumentException("not a product")

  val terms = cddr(p)
  if (length(terms) == 1)
    car(terms)
  else
    makeProducts(terms)
}

def base(b: SD): SD = if(isExponentiation(b)) cadr(b) else throw new IllegalArgumentException("not an exponentiation")

def exponent(e: SD): SD = if(isExponentiation(e)) caddr(e) else throw new IllegalArgumentException("not an exponentiation")

def deriv(exp: SD, variable:Symbol): SD =
  if(isNumber(exp))
    0
  else if(isVariable(exp))
    if(isSameVariable(exp, variable)) 1 else 0
  else if(isSum(exp))
    makeSum(deriv(addend(exp), variable), deriv(augend(exp), variable))
  else if(isProduct(exp))
    makeSum(
      makeProduct(multiplier(exp), deriv(multiplcand(exp), variable)),
      makeProduct(deriv(multiplier(exp), variable), multiplcand(exp))
    )
  else if(isExponentiation(exp))
      makeProduct(
        exponent(exp),
        makeProduct(makeExponentiation(base(exp), sum(exponent(exp), -1)),
          deriv(base(exp), variable))
      )
  else
    throw new IllegalArgumentException(s"unknown expression type: $exp")


deriv(SchemeList('+, 'x, 3), 'x)
deriv(SchemeList(Symbol("*"), Symbol("x"), Symbol("y")), Symbol("x"))
deriv(SchemeList(Symbol("**"), Symbol("x"), 6), Symbol("x"))

deriv(SchemeList(Symbol("+"), Symbol("x"), 1, Symbol("x")), Symbol("x"))
deriv(SchemeList(Symbol("*"), Symbol("x"), 1, Symbol("x")), Symbol("x"))







