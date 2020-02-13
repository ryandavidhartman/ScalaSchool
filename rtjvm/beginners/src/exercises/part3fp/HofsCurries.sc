//
//  toCurry(f: (Int, Int) => Int) yields (Int => Int => Int)
//

def adder(x:Int, y:Int) = x+y

def toCurry(f: (Int, Int) => Int) = (x:Int) => f(x, _)
val add5 = toCurry(adder)(5)
add5(3)

// fromCurry(f: (Int => Int => Int)) yields (Int, Int) => Int

def fromCurry(f: (Int => Int => Int)) = (x:Int, y:Int) => f(x)(y)
def curriedAdder(x:Int)(y:Int) = x + y

def regularAdd = fromCurry(curriedAdder)
regularAdd(7,8)

// compose(f,g) yields  x => f(g(x))
// andThen(f,g) yields x => g(f(x))
def doubler(x:Int) = 2*x
def squarer(x:Int) = x*x

def compose(f: Int => Int, g: Int => Int) = (x:Int) => f(g(x))
def andThen(f: Int => Int, g: Int => Int) = (x:Int) => g(f(x))

compose(doubler, squarer)(2)
andThen(doubler, squarer)(2)



