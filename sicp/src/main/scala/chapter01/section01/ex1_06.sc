/*
Exercise 1.6

Alyssa P. Hacker doesn't see why if needs to be provided as a special form.
``Why can't I just define it as an ordinary procedure in terms of cond?'' she asks. Alyssa's friend
Eva Lu Ator claims this can indeed be done, and she defines a new version of if:

(define (new-if predicate then-clause else-clause)
  (cond (predicate then-clause)
        (else else-clause)))

Eva demonstrates the program for Alyssa:

(new-if (= 2 3) 0 5)
5

(new-if (= 1 1) 0 5)
0

Delighted, Alyssa uses new-if to rewrite the square-root program:

(define (sqrt-iter guess x)
  (new-if (good-enough? guess x)
          guess
          (sqrt-iter (improve guess x)
                     x)))

What happens when Alyssa attempts to use this to compute square roots? Explain.

 */
// Answer:
/* when you use the new-if procedure (which is NOT a special form) i.e. It evaluates using the Scheme's standard
applicative order evaluation model.

So in
(define (sqrt-iter2 guess x)
  (new-if (good-enough? guess x)
          guess
          (sqrt-iter2 (improve guess x)
                     x)))

Both branches of the predicate will be evaluated i.e. guess and (sqrt-iter2 (improve guess x) x)) this causes
the recursion to loop forever
*/

/*
(define (average x y)
  (/ (+ x y) 2))

(define (good-enough? guess x)
  (< (abs (- (square guess) x)) 0.001))

(define (improve guess x)
  (average guess (/ x guess)))

(define (sqrt-iter guess x)
  (if (good-enough? guess x)
      guess
      (sqrt-iter (improve guess x)
                 x)))


(define (mySqrt x)
  (sqrt-iter 1.0 x))

(mySqrt 9)


(define (new-if predicate then-clause else-clause)
  (cond (predicate then-clause)
        (else else-clause)))


(new-if (= 2 3) 0 5)
(new-if (= 1 1) 0 5)


(define (sqrt-iter2 guess x)
  (new-if (good-enough? guess x)
          guess
          (sqrt-iter2 (improve guess x)
                     x)))

(define (mySqrt2 x)
  (sqrt-iter2 1.0 x))

(mySqrt2 9)
 */
