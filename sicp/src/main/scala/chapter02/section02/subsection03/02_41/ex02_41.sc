import ScalaScheme.Primitives.{SD, SL, cons, filter, flat_map, map, car, cdrL, isNull, sum}
import ScalaScheme.{SchemeList, SchemeNil}
// solve it the idiomatic scala way

def triples(n: Int, s: Int): Seq[(Int, Int, Int)] =
  for {
    i <- 1 to n
    j <- 1 to n
    k <- 1 to n if i+j+k == s
  } yield  (i, j, k)

triples(3, 7)

// triples desugared scala

def triples2(n: Int, s: Int): Seq[(Int, Int, Int)] =
  (1 to n).flatMap{ i =>
    (1 to n).flatMap{ j  =>
      (1 to n).withFilter{k1 => i+j+k1 == s}.map{ k =>
        (i,j,k)
      }
    }
  }

triples2(3,7)

// triples in scala scheme format

def enumerate_interval(i: Int, j: Int): SL =
  if ( i > j)
    SchemeNil
  else
    cons (i,enumerate_interval(i+1, j))


def triples3(n: Int, s: Int): SD =
  filter(sl => sum(sl) == s,
    flat_map(i =>
      flat_map(j =>
        map(k => SchemeList(i,j,k),
            enumerate_interval(1,n)),
        enumerate_interval(1, n)),
      enumerate_interval(1, n)).asInstanceOf[SL])

triples3(3,7)


