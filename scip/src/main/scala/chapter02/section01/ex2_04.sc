/*
Exercise 2.4.

Here is an alternative procedural representation of pairs. For this representation,
verify that (car (cons x y)) yields x for any objects x and y.


(define (cons x y)
(lambda (m) (m x y)))

(define (car z)
(z (lambda (p q) p)))

What is the corresponding definition of cdr? (Hint: To verify that this works, make use of the substitution model
of section 1.1.5.)
 */

type pair[T] = ((T, T) => T) => T

def cons[T](x: T, y: T): pair[T] = (m: (T, T) => T)=> m(x,y)

def car[T](z: pair[T]): T = z((x,_) => x )

def cdr[T](z: pair[T]): T = z((_, y) => y)

val pair1 = cons(1,true)
car(pair1)
cdr(pair1)


