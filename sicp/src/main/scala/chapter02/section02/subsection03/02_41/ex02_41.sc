import ScalaScheme.Primitives.{SD, SL, cons, filter, flat_map, map, car, cdrL, isNull}
import ScalaScheme.{SchemeList, SchemeNil}
import ScalaScheme.SchemeMath.sum

// solve it the idiomatic scala way

def triples(n: Int, s: Int): Seq[(Int, Int, Int)] =
  for {
    i <- 1 to n
    j <- 1 until i
    k <- 1 until j if i+j+k == s
  } yield  (i, j, k)

triples(10, 10)

// triples desugared scala

def triples2(n: Int, s: Int): Seq[(Int, Int, Int)] =
  (1 to n).flatMap{ i =>
    (1 to n).flatMap{ j  =>
      (1 to n).withFilter{k1 => i+j+k1 == s}.map{ k =>
        (i,j,k)
      }
    }
  }

triples2(10,10)

// triples in scala scheme format

def enumerate_interval(i: Int, j: Int): SL =
  if ( i > j)
    SchemeNil
  else
    cons (i,enumerate_interval(i+1, j))

def summer(data:SD): Int = {
  val seq = data.asInstanceOf[SL]
  if (isNull(seq))
    0
  else
    car(seq).asInstanceOf[Int] + summer(cdrL(seq))
}

def triples3(n: Int, s: Int): SD =
  filter(sl => summer(sl) == s,
    flat_map(i =>
      flat_map(j =>
        map(k => SchemeList(i,j,k),
            enumerate_interval(1,n)),
        enumerate_interval(1, n)),
      enumerate_interval(1, n)).asInstanceOf[SL])

triples3(3,7)


