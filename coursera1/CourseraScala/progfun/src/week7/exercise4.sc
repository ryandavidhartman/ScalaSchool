object exercise4 {
  def from(n:Int): Stream[Int] = n #:: from(n+1)

  val naturalNumbers = from(0)
  val multiplesOfFour = naturalNumbers map (_ * 4)

  for(i <- (1 to 10)) println(s"${naturalNumbers(i)}")
  naturalNumbers
  /*
  The Sieve of Eratosthenes is an ancient technique to calculate prime numbers.
  The idea is as follows:
  Start with all integers from 2 (The first prime number)
  ► Eliminate all multiples of 2.
  ► The first element of the resulting list is 3, a prime number.
  ► Eliminate all multiples of 3.
  ► Iterate forever.
  At each step, the first number in the list is a prime number and we eliminate
  all its multiples
  */

  def sieve(s: Stream[Int]): Stream[Int] =
    s.head #:: sieve(s.tail filter (_ % s.head !=0))

  val primes = sieve(from(2))

  for(i <- (1 to 10)) println(s"${primes(i)}")

  primes

  def sqrtStream(x: Double): Stream[Double] = {
    def improve(guess: Double) = (guess + x / guess) / 2
    lazy val guesses: Stream[Double] = 1 #:: (guesses map improve)
    guesses
  }

  sqrtStream(16).take(8).toList
  def isGoodEnough(guess:Double, x:Double) =
    math.abs((guess * guess -x)/x) < 0.0001
  sqrtStream(4).filter(isGoodEnough(_,4)).take(10).toList
  sqrtStream(16).filter(isGoodEnough(_,16)).take(10).toList
}