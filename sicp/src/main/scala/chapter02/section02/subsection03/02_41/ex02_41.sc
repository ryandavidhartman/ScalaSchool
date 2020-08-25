import ScalaScheme.{SchemeList, SchemeNil}
import ScalaScheme.Primitives.{SD, SL, cons, flat_map, fold_right,map, filter}
import ScalaScheme.Primitives.schemeDataToSchemeList

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

def isCorrectSum(data:SL, sum: Int): Boolean = {
  sum == fold_right((i,j) => i.toString.toInt + j.toString.toInt, 0, data)
}

def triples3(n: Int, s: Int): SD =
  filter(sl => isCorrectSum(sl, s),
    flat_map(i =>
      flat_map(j =>
        map(k => SchemeList(i,j,k),
            enumerate_interval(1,n)),
        enumerate_interval(1, n)),
      enumerate_interval(1, n)))

triples3(6,7)


