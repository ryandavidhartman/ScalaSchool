import ScalaScheme.Primitives._
import ScalaScheme.{SchemeList => SL}

def count_leaves(x: SD): Int =
  if(isNull(x))
    0
  else if(!isPair(x))
    1
  else {
    val list = x.asInstanceOf[SL]
    count_leaves(car(list)) + count_leaves(cdrL(list))
  }

val tree1 = cons(SL(1, 2), SL(3,4))

tree1.length


count_leaves(tree1)