/* Exercise 2.28

Write a procedure fringe that takes as argument a tree (represented as a list) and returns a list whose elements are
all the leaves of the tree arranged in left-to-right order. For example,

 (define x (list (list 1 2) (list 3 4)))

 (fringe x)
 (1 2 3 4)

 (fringe (list x x))
 (1 2 3 4 1 2 3 4)

  */

val x = Seq(Seq(1,2), Seq(3,4))

def myFlatten(seq: Seq[Any]): Seq[Any] = seq match {
  case Nil => Nil
  case x :: Nil => x match {
    case ys:Seq[Any] => myFlatten(ys)
    case _:Any => seq
  }
  case x :: xs => x match {
    case y:Seq[Any] => myFlatten(y) ++ myFlatten(xs)
    case y:Any => y +: myFlatten(xs)
  }
}

myFlatten(x)
myFlatten(Seq(Seq(x,x), x))


