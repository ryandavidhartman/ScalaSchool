import week4._
object exercise4 {
  //eval(Sum(Num(1),Num(2)) = 3

  /*
  def eval(e:Expr): Int = {
    if (e.isNumber) e.numValue
    else if (e.isSum) eval(e.leftOp) + eval(e.rightOp)
    else throw new Error("Unknown expression " + e)
  }*/
  val bob = Number(1)
  val sally = Sum(Number(5), Number(10))
  sally.eval
  sally.show
  val ryan = Var("X")
  ryan.show
  val test1 = Sum(Prod(Number(2),Var("x")), Var("y"))
  test1.show
  val test2 = Prod(Sum(Number(2),Var("x")), Var("y"))
  test2.show
  var test3 = Sum(test1, test2)
  test3.show


}