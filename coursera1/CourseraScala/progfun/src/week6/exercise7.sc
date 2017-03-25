object exercise7 {
  val romanNumerals = Map("I" -> 1, "V" -> 5, "X" -> 10)
  val capitalOfCountry = Map("US" -> "Washington", "Switerland" -> "Bern")

  capitalOfCountry("US")
  //capitalOfCountry("Andorra")
  capitalOfCountry get "Andorra"
  val bob =capitalOfCountry get "US"

  def showCapital(country: String) = capitalOfCountry.get(country) match {
    case Some(capital) => capital
    case None => s"$country not found"
  }

  showCapital("Ryan")
  showCapital("Switerland")

  val fruit = List("apple", "pear", "orange", "pineapple")
  fruit sortWith(_.length < _.length)
  fruit.sorted
  fruit groupBy(_.head)

  //Polynomials x^3 -2*x +5 is represented as Map(0->5, 1->-2, 3->1)

  class Poly(val terms: Map[Int, Double]) {
    def +(other: Poly) = new Poly(terms ++ (other.terms map adjust))
    def adjust(term: (Int, Double)): (Int, Double) = {
      val (exp, coeff) = term
      terms get exp match {
        case Some(coeff1) => exp -> (coeff + coeff1)
        case None => exp -> coeff
      }
    }
    override def toString() = (for ((exp, coeff) <- terms.toList.sorted.reverse) yield coeff+"x^"+exp) mkString " + "
  }

  val p1 = new Poly(Map(1 -> 2.0, 3 -> 4.0, 5 -> 6.2))
  val p2 = new Poly(Map(0 -> 3.0, 3 -> 7.0))
  p1+p2

  class Poly2(terms0: Map[Int, Double]) {
    val terms = terms0 withDefaultValue 0.0

    def +(other: Poly2) = new Poly2(terms ++ (other.terms map adjust))

    def adjust(term: (Int, Double)): (Int, Double) = {
      val (exp, coeff) = term
      exp -> (coeff + terms(exp))
    }
    override def toString() = (for ((exp, coeff) <- terms.toList.sorted.reverse) yield coeff + "x^" + exp) mkString " + "
  }

    val p3 = new Poly2(Map(1 -> 2.0, 3 -> 4.0, 5 -> 6.2))
    val p4 = new Poly2(Map(0 -> 3.0, 3 -> 7.0))
    p3+p4

  class Poly3(terms0: Map[Int, Double]) {
    def this(bindings: (Int,Double)*) = this(bindings.toMap)
    val terms = terms0 withDefaultValue 0.0

    def +(other: Poly3) = new Poly3(terms ++ (other.terms map adjust))

    def adjust(term: (Int, Double)): (Int, Double) = {
      val (exp, coeff) = term
      exp -> (coeff + terms(exp))
    }
    override def toString() = (for ((exp, coeff) <- terms.toList.sorted.reverse) yield coeff + "x^" + exp) mkString " + "
  }

  val p5 = new Poly3(1 ->2.0, 3 -> 4.0, 5 -> 6.2)
  val p6 = new Poly3(0 -> 3.0, 3 -> 7.0)
  p3+p4

  val bobo = 1 -> 1.5
  val bobo2 = (1,1.5)

  class Poly4(terms0: Map[Int, Double]) {
    def this(bindings: (Int,Double)*) = this(bindings.toMap)
    val terms = terms0 withDefaultValue 0.0

    def +(other: Poly4) = new Poly4((other.terms foldLeft terms)(addTerm))

    def addTerm(terms: Map[Int, Double], term:(Int, Double)): Map[Int, Double] = {
      val (exp, coeff) = term
      terms + (exp -> (coeff + terms(exp)))
    }

    def adjust(term: (Int, Double)): (Int, Double) = {
      val (exp, coeff) = term
      exp -> (coeff + terms(exp))
    }
    override def toString() = (for ((exp, coeff) <- terms.toList.sorted.reverse) yield coeff + "x^" + exp) mkString " + "
  }


}