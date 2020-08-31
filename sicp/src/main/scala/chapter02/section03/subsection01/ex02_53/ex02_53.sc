import ScalaScheme.Primitives.{SD, SL, car, cdrL, isNull}
import ScalaScheme.SchemeList

import scala.annotation.tailrec
import scala.reflect.runtime.currentMirror
import scala.reflect.runtime.universe.Quasiquote
import scala.tools.reflect.ToolBox



@tailrec
def memq(item: SD, x: SL): SD =
  if(isNull(x))
    false
  else if(item == car(x))
    x
  else
    memq(item, cdrL(x))

// (list 'a 'b 'c)
SchemeList(q"a", q"b", q"c")

// (list (list 'george))
SchemeList(SchemeList(q"george"))

// (cdr '((x1 x2) (y1 y2)))
val quoted1 = q"List(List('x1, 'x2), List('y1, 'y2)).tail"
val toolbox = currentMirror.mkToolBox()
toolbox.eval(quoted1).asInstanceOf[List[Any]]


val bob = q"sally"





