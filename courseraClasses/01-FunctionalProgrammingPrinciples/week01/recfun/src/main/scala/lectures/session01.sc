
1 + 3

println("dfd")

def abs(x: Double) = if (x > 0.0) x else -x

def sqrt(x: Double) = {

  def sqrtIter(guess: Double): Double =
    if (isGoodEnough(guess)) guess
    else sqrtIter(improve(guess))

  def isGoodEnough(guess: Double) =
    abs(1 - guess * guess / x) < 0.001

  def improve(guess: Double) =
    (guess + x / guess) / 2

  sqrtIter(1.0)
}

sqrt(4)
sqrt(1e50)
sqrt(1e-20)


class RyanSample(s: String) {
  val something: String = s

  def say: String = something
}

val bob = new RyanSample("Hi")
println(s"Bob says: ${bob.say}")

