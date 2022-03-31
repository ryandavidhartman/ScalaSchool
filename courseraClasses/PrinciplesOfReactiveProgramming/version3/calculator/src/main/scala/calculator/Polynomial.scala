package calculator

object Polynomial {
  def computeDelta(a: Signal[Double], b: Signal[Double],
      c: Signal[Double]): Signal[Double] = {
    Signal(b() * b() - 4 * a() * c())
  }

  /**
   * (-b ± √Δ) / (2a)
   * @param a
   * @param b
   * @param c
   * @param delta
   * @return
   */
  def computeSolutions(a: Signal[Double], b: Signal[Double],
      c: Signal[Double], delta: Signal[Double]): Signal[Set[Double]] = {

    if(delta() < 0.0) {
      Signal(Set[Double]())
    } else {
      if(delta() == 0.0) {
        Signal(Set[Double]((-b()) / (2 * a())))
      } else {
        val left = -b()
        val right = Math.sqrt(delta())
        val denom = 2 * a()
        val sol1 = (left  + right) / (denom)
        val sol2 = (left  - right) / (denom)
        Signal(Set(sol1, sol2))
      }
    }
    //val delta = Signal(Math.abs(computeDelta(a,b,c)()))

  }
}
