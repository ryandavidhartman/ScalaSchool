object exercise2 {
  val st1 = (1 to 1000).toStream

  def streamRange(low: Int, high: Int): Stream[Int] =
    if (low >= high) Stream.empty
    else Stream.cons(low, streamRange(low + 1, high))


  val st2 = streamRange(1, 1000)

  def listRange(low: Int, high: Int): List[Int] =
    if (low >= high) Nil
    else low :: listRange(low + 1, high)

  val st3 = listRange(1, 1000)

  def isPrime(n: Int): Boolean = (2 until n) forall (i => n % i != 0)

  isPrime(3)

  val bob = ((1000 to 10000).toStream filter isPrime)(1)

  trait myStream[T] {
    def isEmpty: Boolean
    def head: T
    def tail: myStream[T]
  }

  object myStream {
    def cons[T](hd: T, tl: => myStream[T]) = new myStream[T] {
      def isEmpty: Boolean = false
      def head = hd
      def tail = tl
    }
  }

  object myEmpty extends myStream[Nothing] {
    def isEmpty: Boolean = true
    def head: Nothing = throw new NoSuchElementException("empty.head")
    def tail: Nothing = throw new NoSuchElementException("empty.tail")

  }

}