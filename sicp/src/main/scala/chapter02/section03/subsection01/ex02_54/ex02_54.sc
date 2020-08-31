import ScalaScheme.Primitives.{SD, car, cdrL, isPair, schemeDataToSchemeList}
import ScalaScheme.{SchemeList, SchemeNil}
import scala.annotation.tailrec
import scala.reflect.runtime.currentMirror
import scala.reflect.runtime.universe.Quasiquote
import scala.tools.reflect.ToolBox

/*
n practice, programmers use equal? to compare lists that contain numbers as well as symbols.
Numbers are not considered to be symbols. The question of whether two numerically equal numbers
(as tested by =) are also eq? is highly implementation-dependent.
A better definition of equal? (such as the one that comes as a primitive in Scheme)
would also stipulate that if a and b are both numbers, then a and b are equal? if they are
numerically equal.
 */

def isEqual(a: SD, b:SD): Boolean =
  if (isPair(a) && isPair(b))
    isEqual(car(a), car(b)) && isEqual(cdrL(a), cdrL(b))
  else if (!isPair(a) && !isPair(b))
    a.equals(b)
  else
    false

isEqual(5,6)
isEqual(5,5)
isEqual('a,'b)
isEqual('a,'a)
isEqual(SchemeList('b), SchemeList('b))
isEqual(SchemeList(6), SchemeList(6))
isEqual(SchemeList(6, 1, 3), SchemeList(6, 1, 3))
isEqual(SchemeList(6, 1, SchemeList(3, 2, 1)), SchemeList(6, 1, 3))
isEqual(SchemeList(6, 1, SchemeList(3, 2, 1)), SchemeList(6, 1, SchemeList(3, 2, 1)))
isEqual(SchemeList(1,2,3), 5)
isEqual(5, SchemeList(1,2,3))
isEqual(SchemeList(5), SchemeList(1,2,3))
isEqual(SchemeList(SchemeList(SchemeList(1))), SchemeList(SchemeList(SchemeList(1))))

