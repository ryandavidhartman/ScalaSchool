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

def unique_pairs_scheme(n: Int): SL = {
  val list = flat_map(
    i => map(j => SchemeList(i,j), enumerate_interval(1, i.asInstanceOf[Int]-1)),
    enumerate_interval(1, n))
  list.asInstanceOf[SL]
}

unique_pairs_scheme(10)

def isPrimeSum(p: SchemePair): Boolean = isPrime(sum(car(p), cadr(p)).toInt)

def make_pair_sum(p: SchemePair): SL =
  SchemeList(car(p), cadr(p), sum(car(p), cadr(p)).toInt)

/*
Scheme
(define (prime-sum-pairs2 n)
  (map make-pair-sum
       (filter prime-sum?
               (unique-pairs n))))
 */

def prime_sum_pairs(n: Int): SD =
  map(i => make_pair_sum(i),
    filter(j => isPrimeSum(j),
      unique_pairs_scheme(n))
)

prime_sum_pairs(6)

