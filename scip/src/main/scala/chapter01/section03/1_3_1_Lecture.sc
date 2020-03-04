def cube[T](x:T)(implicit num: Numeric[T]):T = num.times(x, num.times(x,x))

def sum_integers(a:Int, b:Int): Int =
if (a > b)
  0
else
  a + sum_integers(a+ 1,  b)

sum_integers(1,3)

def sum_cubes(a:Int, b:Int): Int =
  if (a > b)
    0
  else
    cube(a) + sum_cubes(a+ 1,  b)

sum_cubes(1,3)


def pi_sum(a:Int, b:Int): Double =
  if (a > b)
    0
  else
    1.0/(a * (a+2)) + pi_sum(a+4,  b)

pi_sum(1,10000)*8


// Make this a generic concept:
def sum[T,U](term: T=>U, a:T, next: T=>T, b: T)(implicit numU: Numeric[U], numT: Numeric[T]): U = {
  if (numT.gt(a,b))
    numU.zero
  else
    numU.plus(term(a), sum(term, next(a), next, b))
}

def sum_integers2(a:Int, b:Int):Int= sum[Int, Int](x=>x, a, x=>x+1, b)
sum_integers2(1,3)

def sum_cubes2(a:Int, b:Int)= sum[Int, Int](cube, a, x=>x+1, b)
sum_cubes2(1,3)

def pi_sum2(a:Int, b:Int):Double = sum[Int, Double](x=>1.0/(x*(x+2)), a, x=>x+4, b)
pi_sum2(1,10000)*8

def integral(f:Double=>Double, a:Double, b:Double, dx:Double): Double =
  sum[Double,Double](f, a, x => x+dx, b)*dx

integral(cube, 0, 1, 0.01)
integral(cube, 0, 1, 0.001)
