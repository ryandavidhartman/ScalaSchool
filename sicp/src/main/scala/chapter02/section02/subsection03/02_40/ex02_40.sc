import ScalaScheme.Primitives.{SD, SL, car, cdrL, cons, isNull, append}
import ScalaScheme.{SchemeList, SchemeNil}
import ScalaScheme.SchemeMath.multiply
// First with the idiomatic scala way

def unique_pairs_scala(n: Int): Seq[(Int, Int)] =
  for {
    i <- 1 to n
    j <- 1 until i
  } yield (i, j)

unique_pairs_scala(5)

// or if you prefer

def unique_pairs_1(n: Int): Seq[(Int, Int)] =
  (1 to n).flatMap(i => (1 until i).map(j => (i,j)))


///////////////////////////////////////////////////////////////////////////////
//  Lets build it up one step and a time like the book                       //
///////////////////////////////////////////////////////////////////////////////

def map(op: SD => SD, seq: SL): SL =
  if(isNull(seq))
    SchemeNil
  else cons(op(car(seq)), map(op, cdrL(seq)))

var list1 = SchemeList(1,2,3,4,5)
map((i: SD) => multiply(i,2), list1 )


def accumulate(op: (SD, SD) => SD, initial: SD, seq: SL): SD =
  if(isNull(seq))
    initial
  else
    op(car(seq), accumulate(op, initial, cdrL(seq)))

accumulate((i: SD, j:SD) => multiply(i,j), 1.0, list1)

def appender(s1:SD, s2:SD): SD = append(s1.asInstanceOf[SL], s2.asInstanceOf[SL])
def flat_map(op: SD => SD, seq: SL): SD = accumulate(appender, SchemeNil, map(op, seq) )
flat_map(i => SchemeList(i), list1)

def enumerate_interval(i: Int, j: Int): SL =
if ( i > j)
  SchemeNil
else
  cons (i,enumerate_interval(i+1, j))

enumerate_interval(1, 5)

def unique_pairs_scheme(n: Int) =
flat_map(i => map(j => SchemeList(i,j), enumerate_interval(1, i.asInstanceOf[Int]-1)),
enumerate_interval(1, n))

unique_pairs_scheme(10)

def filter(op: SD => Boolean, seq: SL): SL =
  if(isNull(seq))
    SchemeNil
  else if(op(car(seq)))
    cons(car(seq), filter(op, cdrL(seq)))
  else
    cdrL(seq)

def prime_sum_pairs(n: SD):SD = ???

