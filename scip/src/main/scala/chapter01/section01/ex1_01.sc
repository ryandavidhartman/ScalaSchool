/*
Exercise 1.1.
Below is a sequence of expressions.
What is the result printed by the interpreter in response to each
expression?

Assume that the sequence is to be evaluated in the order in which
it is presented.
 */


10

5 + 3  + 4 // (+ 5 3 4)

9 - 1 // (- 9 1)

6 / 2  // (/ 6 2)

(2*4) + (4 - 6)  // (+ (* 2 4) (- 4 6))

def a = 3 // (define a 3)
a

def b= a + 1  // (define b (+ a 1))
b

a + b + (a * b) // (+ a b (* a b))

a == b  // (= a b)

/*
(if (and (> b a) (< b (* a b)))
    b
    a)
 */

if( b > a && b < a*b)
  b
else
  a

/*
(cond ((= a 4) 6)
      ((= b 4) (+ 6 7 a))
      (else 25))
 */


if( a == 4)
  6
else if(b == 4)
  6 + 7 + a
else
  25

2 + (if(b>a) b else a)      // (+ 2 (if (> b a) b a))

/*
(* (cond ((> a b) a)
         ((< a b) b)
         (else -1))
   (+ a 1))
 */

(if (a>b) a else if(a < b) b else -1) * (a + 1)





