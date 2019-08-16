
def myInit[T](xs:List[T]) : List[T] = xs match {
  case List() => throw new Error("init of empty list")
  case List(x) => List()
  case y :: ys => y :: myInit(ys)
}
val bob = List(1, 2, 3, 4, 5)
bob.init
myInit(bob)
val sally = List(6,7,8)
def myConcat[T](xs:List[T], ys:List[T]):List[T] = xs match {
  case List() => ys
  case z :: zs => z :: myConcat(zs, ys)
}
myConcat(bob, sally)
def myReverse[T](xs:List[T]) : List[T] = xs match {
  case List() => xs
  case y :: ys => myReverse(ys) ::: List(y)
}
myReverse(bob)

def myRemoveAt[T](index:Int, xs:List[T]) : List[T] = xs match {
  case List() => xs
  case y :: ys => if(index == 0) ys else y :: myRemoveAt(index-1,ys)
}

myRemoveAt(1, bob)

def myRemoveAt2[T](index:Int, xs:List[T]) : List[T] = xs.take(index) ::: xs.drop(index+1)

myRemoveAt2(1, bob)


def myFlatten(list: List[Any]) : List[Any] = list match {
  case List() => Nil
  case (head:List[Any]) :: tail => myFlatten(head) ::: myFlatten(tail)
  case head :: tail => head :: myFlatten(tail)

}

val nick = List(List(1,2,3), List(8,9))
myFlatten(nick)



