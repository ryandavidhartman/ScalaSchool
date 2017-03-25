object exercise6 {

  // you can write map, flatMap and filter in terms of for

  def mapfun[T, U](xs: List[T], f: T => U): List[U] =
    for (x <- xs) yield f(x)

  def flatMap[T, U](xs: List[T], f: T => Iterable[U]): List[U] =
    for (x <- xs; y <- f(x)) yield y

  def filter[T](xs: List[T], p: T =>  Boolean): List[T] =
    for (x <- xs if p(x)) yield x

  val bob = List("a", "b", "c")
  mapfun(bob, (i: String) => List(i.toUpperCase))
  flatMap(bob, (i: String) => List(i.toUpperCase))
  // but really scala for-expressions are written in terms of map, flatMap and a lazy filter
  //
  // for(x <- e1) yield e2 is converted to e1.map(x => e2)
  //
  // for(x <- e1 if f; s) yield e2 is converted to
  // for(x <- e.withFilter(x => f); s) yield e2

  // for(x <- e1 if y <- e2; s) yield e3 is converted to
  // e1`.flatMap(x => for ( y <-e2; s) yield e3)

  //examples

  def isPrime(n: Int): Boolean = (2 until n).forall(i => n%i != 0)

  def mySumIsPrime(n: Int): Seq[(Int, Int)] =
   for {
     i <- 1 until n
     j <- 1 until i
     if isPrime(i+j)
   } yield (i,j)

  val steve = mySumIsPrime(7)
  steve(1)

  // we can translate mySumIsPrime using map. flatMap and filters
  //get rid of the first for
  def mySumIsPrime2(n: Int): Seq[(Int, Int)] =
    (1 until n) flatMap(i => for ( j <- (1 until i) if isPrime(i+j)) yield(i,j))

  val steve2 = mySumIsPrime2(7)

  // get rid of the second for
  def mySumIsPrime3(n: Int): Seq[(Int, Int)] =
    (1 until n) flatMap(i =>
      (1 until i).withFilter(j => isPrime(i+j))
      .map(j => (i,j)))


  val steve3 = mySumIsPrime3(7)

  // another example:
  case class Book(title: String, authors: List[String])
  val books: Set[Book] = Set(
    Book(title = "Structure and Interpretation of Computer Programs",
      authors = List("Abelson, Harald", "Sussman, Gerald J.")),
    Book(title = "Introduction to Functional Programming",
      authors = List("Bird, Richard", "Wadler, Phil")),
    Book(title = "Effective Java",
      authors = List("Bloch, Joshua")),
    Book(title = "Java Puzzlers",
      authors = List("Bloch, Joshua", "Gafter, Neal")),
    Book(title = "Java Puzzlers 2",
      authors = List("Bloch, Joshua", "Gafter, Neal")),
    Book(title = "Programming in Scala",
      authors = List("Odersky, Martin", "Spoon, Lex", "Venners, Bill")))


  for(b <- books; a <- b.authors if a startsWith "Bird,")
    yield b.title

  // translate this to use map, flatmap and filters

  books flatMap(b => for(a <- b.authors; if a startsWith("Bird,")) yield b.title)

  books flatMap(b => b.authors.withFilter(a => a startsWith("Bird,")) map (a => b.title) )

  books flatMap(b => b.authors.withFilter(a => a startsWith("Bloch,")) map (a => b.title) )
}