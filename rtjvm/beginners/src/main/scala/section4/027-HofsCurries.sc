// toCurry(f: (Int, Int) => Int) yields (Int => Int => Int)
// fromCurry(f: (Int => Int => Int)) yields (Int, Int) => Int
// compose(f,g) yields  x => f(g(x))
// andThen(f,g) yields x => g(f(x))
// see lectures/part3fp/026-HofsCurries.scala




def toCurry(f: (Int, Int) => Int) = (x:Int) => f(x, _)
def fromCurry(f: Int => Int => Int) = (x:Int, y:Int) => f(x)(y)
def compose(f: Int => Int, g: Int => Int) = (x:Int) => f(g(x))
def andThen(f: Int => Int, g: Int => Int) = (x:Int) => g(f(x))


def adder(x:Int, y:Int) = x+y

val add5 = toCurry(adder)(5)
add5(3)

val adderBack = fromCurry(toCurry(adder))
adderBack(3,5)


def curriedAdder(x:Int)(y:Int) = x + y

def regularAdd = fromCurry(curriedAdder)
regularAdd(7,8)


def doubler(x:Int) = 2*x
def squarer(x:Int) = x*x



compose(doubler, squarer)(2)
andThen(doubler, squarer)(2)
