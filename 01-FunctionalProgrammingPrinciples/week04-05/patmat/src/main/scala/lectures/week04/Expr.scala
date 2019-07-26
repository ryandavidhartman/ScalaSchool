package week04


trait Expr {
  def isValueType:Boolean

  def eval: Int = this match {
    case Number(n) => n
    case Sum(e1:Expr, e2:Expr) => e1.eval + e2.eval
    case Prod(e1:Expr, e2:Expr) => e1.eval * e2.eval
  }

  def show: String = this match {
    case Number(n) => n.toString
    case Var(x) => x
    case Sum(e1: Expr, e2: Expr) => showHelper(e1, e2, " + ")
    case Prod(e1: Expr, e2: Expr) =>showHelper(e1,e2, "*")
    }

  private def showHelper(e1: Expr, e2: Expr, operationString:String) = {
    val left = if(e1.isValueType  || operationString == " + ") e1.show
               else "(" + e1.show + ")"

    val right= if(e2.isValueType || operationString == " + ") e2.show else "(" + e2.show + ")"


    left + operationString + right
  }
}

case class Number(n:Int) extends Expr
{
  def isValueType = true
}
case class Var(x:String) extends Expr
{
  def isValueType = true
}

//new Sum(e1,e1) is equal to e1 + e2
case class Sum(e1:Expr, e2:Expr) extends Expr
{
  def isValueType = false
}

//new Sum(e1,e1) is equal to e1 * e2
case class Prod(e1:Expr, e2:Expr) extends Expr
{
  def isValueType = false
}






