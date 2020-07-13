import ScalaScheme.{Primitives, SchemePair, SchemeList => SL, SchemeNil => SNil}
import ScalaScheme.Primitives.{SchemeData => SD}
import ScalaScheme.Primitives._


def fold_right(op: (SD, SD) => SD, initial: SD, sequence: SL): SD =
  if (isNull(sequence))
    initial
  else
    op(car(sequence), fold_right(op, initial, cdrL(sequence)))

def fold_left(op: (SD, SD) => SD, initial: SD, sequence: SL): SD = {
  @scala.annotation.tailrec
  def iter(result: SD, rest: SL): SD =
    if (isNull(rest))
      result
    else
      iter(op(result, car(rest)), cdrL(rest))

  iter(initial, sequence)
}

def reverse1(seq: SL) = fold_right((l, r) => append(r.asInstanceOf[SL], SL(l)), SNil, seq)

def reverse2(seq: SL)= fold_left((l, r) => cons(r, l), SNil, seq)

val list1 = SL(1, 2, 3, 4, 5, 6)

reverse1(list1)
reverse2(list1)