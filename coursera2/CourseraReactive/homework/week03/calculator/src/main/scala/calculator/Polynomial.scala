package calculator

object Polynomial {
  def computeDelta(a: Signal[Double], b: Signal[Double],
      c: Signal[Double]): Signal[Double] = {

    Var(math.pow(b(),2) - 4.0*a()*c())
  }

  def computeSolutions(a: Signal[Double], b: Signal[Double],
      c: Signal[Double], delta: Signal[Double]): Signal[Set[Double]] = {

    Var {
      if (delta() >= 0 && a() != 0) {
        val result1: Double = (-b() + math.sqrt(delta())) / (2 * a())
        val result2: Double = (-b() - math.sqrt(delta())) / (2 * a())
        Set(result1, result2)
      } else {
        Set()
      }
    }
  }
}
