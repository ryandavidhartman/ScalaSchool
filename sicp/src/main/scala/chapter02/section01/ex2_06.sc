/* Exercise 2.0
In case representing pairs as procedures wasn't mind-boggling enough, consider that, in a language that can manipulate
procedures, we can get by without numbers (at least insofar as non-negative integers are concerned) by implementing 0
and the operation of adding 1 as

(define zero (lambda (f) (lambda (x) x)))

(define (add-1 n)
  (lambda (f) (lambda (x) (f ((n f) x)))))

This representation is known as Church numerals, after its inventor, Alonzo Church, the logician who invented the
Lambda calculus.

Define one and two directly (not in terms of zero and add-1).
(Hint: Use substitution to evaluate (add-1 zero)).

Give a direct definition of the addition procedure + (not in terms of repeated application of add-1).
 */

type Func[T] = T => T
type Num[T] = Func[T] => Func[T]

def zero[T]: Num[T] = (_: Func[T]) => (x: T) => x
def add_1[T] (n: Num[T]): Num[T] = (f: Func[T]) => (x: T) => f( n(f)(x) )

// def one[T]: Num[T] = add_1(zero)
// def one[T]: Num[T] = (f: Func[T]) => (x: T) => f( zero(f)(x) )
// def one[T]: Num[T] = (f: Func[T]) => (x: T) => f( ((_: Func[T]) => (x1: T) => x1)(f)(x) )
// def one[T]: Num[T] = (f: Func[T]) => (x: T) => f( ((x1: T) => x1)(x) )
def one[T]: Num[T] = (f: Func[T]) => (x: T) => f(x)

// def two[T]: Num[T] = add_1(one)
// def two[T]: Num[T] = (f: Func[T]) => (x: T) => f( one(f)(x) )
// def two[T]: Num[T] = (f: Func[T]) => (x: T) => f( ((f1: Func[T]) => (x1: T) => f1(x1))(f)(x) )
// def two[T]: Num[T] = (f: Func[T]) => (x: T) => f( ((x1: T) => f(x1))(x) )
def two[T]: Num[T] = (f: Func[T]) => (x: T) => f(f(x))
def three[T]: Num[T] = (f: Func[T]) => (x: T) => f(f(f(x)))

def church_to_int (num: Num[Int]): Int = num((x: Int) => x + 1)(0)
church_to_int(zero)
church_to_int(one)
church_to_int(two)
church_to_int(three)

