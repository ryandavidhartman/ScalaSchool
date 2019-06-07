object exercise1 {
  def isPrime(n:Int):Boolean = (2 until n) forall (i => n % i != 0)
  isPrime(2)
  isPrime(3)
  isPrime(4)
  isPrime(6)
  isPrime(101)
  val v1 = Vector(1.0,2.0,3.0)
  v1 map (i => i*i)
  val s1 = "Hi Ryan!"
  s1 filter (i => i.isUpper)
  s1 filter (_.isUpper)
  s1 exists (_.isUpper)
  s1 forall (_.isUpper)

 val pairs = s1 zip v1
  pairs unzip

  s1 flatMap (i => List('.',i))

  s1 map ( i => '.' + i)

  val bob = (1 to 3) flatMap (x => (1 to 3) map (y => (x,y)))

  def scalarProduct(v1:Vector[Double], v2:Vector[Double]):Double = {
    (v1 zip v2).map(i => i._1 * i._2).sum
  }

  val v2 = Vector(2.0,2.0,2.0)

  scalarProduct(v1,v2)

}