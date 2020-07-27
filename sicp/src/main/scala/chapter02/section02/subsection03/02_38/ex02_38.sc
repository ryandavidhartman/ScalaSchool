import ScalaScheme.Primitives.{SchemeData => SD}
import ScalaScheme.Primitives._
import ScalaScheme.{SchemeNil => SNil, SchemeList => SL}
import ScalaScheme.SchemeMath._


def accumulate(op: (SD, SD) => SD, initial: SD, sequence: SL): SD =
  if(isNull(sequence))
    initial
  else
   op(car(sequence),accumulate(op, initial, cdrL(sequence)))

val fold_right: ((SD, SD) => SD, SD, SL) => SD = accumulate


def fold_left(op: (SD, SD) => SD, initial: SD, sequence: SL): SD = {
  @scala.annotation.tailrec
  def iter(result: SD, rest: SL): SD =
    if(isNull(rest))
      result
    else
      iter(op(result, car(rest)), cdrL(rest))

  iter(initial, sequence)
}

def div(num: SD, denom: SD): SD = division(num, denom)

fold_right(div, 1,  SL(1, 2, 3))
fold_left(div, 1,  SL(1, 2, 3))

def list(l: SD, r: SD): SD = SL(l, r)

fold_right(list,  SNil,  SL(1, 2, 3))
fold_left(list, SNil, SL(1,2,3))

