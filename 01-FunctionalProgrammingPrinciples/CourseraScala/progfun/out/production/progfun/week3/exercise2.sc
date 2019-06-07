import week3._

def singleton[T](elem: T) = new Cons[T](elem, new Nil[T])

val bob = singleton[Int](5)
bob.head

val sally = singleton[Boolean](true)
sally.head

val bob2 = singleton(5)
bob2.head

val sally2 = singleton(true)
sally2.head


def nth[T](n: Integer, list: List[T]): T = {
  if(list.isEmpty || n < 0) throw new IndexOutOfBoundsException("n was out of bounds")
  else if(n == 0) list.head
  else nth(n-1, list.tail)
}


val test = new Cons(3,new Nil[Int])
val test2 = new Cons(2, test)
val test3 = new Cons(1, test2)

val n0 = nth(0,test3)
val n1 = nth(1,test3)
val n2 = nth(2,test3)
val n3 = nth(3,test3)

