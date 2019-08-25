
def sum(f: Int => Int, a: Int, b: Int): Int = {
  def loop(a: Int, acc: Int): Int = {
    if (a>b) acc
    else loop(a+1, f(a)+acc)
  }
  loop(a, 0)
}
sum(x =>x*x,1,2)


def sum2(f: Int => Int): (Int, Int) => Int = {
  def sumF(a: Int, b: Int): Int = {
    if (a>b) 0
    else f(a) + sumF(a+1,b)
  }
  sumF
}

sum2(x=>x)(1,5)

def sum3(f: Int => Int)(a: Int, b: Int): Int = {
  if (a>b) 0 else f(a) + sum3(f)(a+1,b)

}

def product(f:Int => Int)(a:Int, b:Int): Int =
{
  if(a>b) 1 else f(a)*product(f)(a+1,b)
}

product(x=>x)(1,3)

def fact(n: Int): Int = product(x=>x)(1,n)

fact(5)

def mapReduce(f: Int => Int, combine:(Int, Int) => Int, zero:Int)(a: Int, b: Int): Int = {
  if (a > b) zero else combine(f(a), mapReduce(f,combine,zero)(a+1,b))
}

mapReduce(x=>x, (i,j) => i*j, 1)(1,5)

def product2(f:Int => Int)(a:Int, b:Int): Int = mapReduce(f, (x,y) => x*y, 1)(a,b)

product2(x=>x*x)(1,3)
