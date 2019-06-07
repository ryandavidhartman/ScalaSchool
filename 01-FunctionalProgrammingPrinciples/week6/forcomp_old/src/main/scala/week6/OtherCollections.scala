package week6

object Examples{
  //To list all combinations of numbers x and y where x is drawn from 1..M and y is drawn from 1..N:

  val m = 10
  val n = 3

  val combinations = (1 to m) flatMap(x => (1 to n).map (y => (x,y)))

  val combinations2 = for {
    x <- (1 to m)
    y <- (1 to n)
  } yield (x,y)

  def isPrime(n:Int) = (2 until n) forall  (n % _ != 0)
}