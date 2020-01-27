 /*
Exercise 1.10: The following procedure computes a mathematical function called Ackermann’s function.
(define (A x y)
(cond ((= y 0) 0)
((= x 0) (* 2 y))
((= y 1) 2)
(else (A (- x 1) (A x (- y 1))))))
What are the values of the following expressions?
(A 1 10)
(A 2 4)
(A 3 3)
Consider the following procedures, where A is the procedure defined above:
(define (f n) (A 0 n))
(define (g n) (A 1 n))
(define (h n) (A 2 n))
(define (k n) (* 5 n n))
Give concise mathematical definitions for the functions computed by the procedures f, g, and h for positive integer values of n. For example, (k n) computes 5n
2
 */

def Ackermann(x: BigInt, y: BigInt): BigInt =
  if(y == 0) 0
  else if(x == 0) 2*y
  else if(y == 1) 2
  else Ackermann(x-1, Ackermann(x, y-1))

Ackermann(5,0)
Ackermann(2,1)
Ackermann(1, 2)
Ackermann(1,3)
Ackermann(1, 10)
Ackermann(2, 4)
Ackermann(3, 3)

def f(n:Int) = Ackermann(0,n)  // 2 * n
f(1)
f(2)
f(3)

def g(n:Int):BigInt = Ackermann(1,n) // 2^n
g(1)
g(2)
g(3)
g(4)

/*
 Ackermann(1,3)
 Ackermann(0, Ackermann(1, 2))
 Ackermann(0, Ackermann(0, Ackermann(1, 1)))
 Ackermann(0, Ackermann(0, 2))
 Ackermann(0, 2*2)
 2*3*2
 */

def h(n:Int) =  Ackermann(2,n)  // 2^2^2... n times
h(1) //2^1
h(2) //2^2
h(3) // 2^(2^2)
h(4) // 2^(2^(2^2))
h(5)

def k(n:Int) = 5 * n * n // 5n^2




