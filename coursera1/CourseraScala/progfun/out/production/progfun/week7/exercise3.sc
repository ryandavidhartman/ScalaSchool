object exercise3 {
  def expr = {
    val x = {
      println("x"); 1
    }
    lazy val y = {
      println("y"); 2
    }
    def z = {
      println("z"); 3
    }

    z + y + x + z + y + x
  }

  expr

  trait myStream[T] {
    def isEmpty: Boolean
    def head: T
    def tail: myStream[T]
  }

  object myStream {
    def cons[T](hd: T, tl: => myStream[T]) = new myStream[T] {
      def isEmpty: Boolean = false
      def head = hd
      lazy val tail = tl //make this a lazy val
    }
  }

  object myEmpty extends myStream[Nothing] {
    def isEmpty: Boolean = true
    def head: Nothing = throw new NoSuchElementException("empty.head")
    def tail: Nothing = throw new NoSuchElementException("empty.tail")
  }

  def isPrime(n: Int): Boolean = (2 until n) forall (i => n % i != 0)

  val bob = ((1000 to 10000) filter isPrime)(1)

}