import ScalaScheme.Primitives._
import ScalaScheme.Primitives.schemeDataToSchemePair
import ScalaScheme.{SchemeList, SchemeNil, SchemePair}
import ScalaScheme.SchemeMath.{isPrime, multiply, sum}
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

def unique_pairs_scheme(n: Int): SL = {
  val list = flat_map(
    i => map(j => SchemeList(i,j), enumerate_interval(1, i.asInstanceOf[Int]-1)),
    enumerate_interval(1, n))
  list.asInstanceOf[SL]
}

unique_pairs_scheme(10)

def filter(op: SD => Boolean, seq: SL): SL =
  if(isNull(seq))
    SchemeNil
  else if(op(car(seq)))
    cons(car(seq), filter(op, cdrL(seq)))
  else
    filter(op, cdrL(seq))

def isPrimeSum(p: SchemePair): Boolean = isPrime(sum(car(p), cadr(p)).toInt)

def make_pair_sum(p: SchemePair): SL =
  SchemeList(car(p), cadr(p), sum(car(p), cadr(p)).toInt)

def prime_sum_pairs(n: Int): SD = map(
  i => make_pair_sum(i),
  filter(j => isPrimeSum(j), unique_pairs_scheme(n))
)

prime_sum_pairs(6)

