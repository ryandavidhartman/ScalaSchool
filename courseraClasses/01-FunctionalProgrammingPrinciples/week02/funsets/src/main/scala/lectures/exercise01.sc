
def factorial(n: Int): Int = {
  def loop(acc: Int, n:Int): Int = {
    if(n == 0)
      acc
    else
      loop(acc*n,n-1)
  }

  loop(1,n)
}

factorial(5)
