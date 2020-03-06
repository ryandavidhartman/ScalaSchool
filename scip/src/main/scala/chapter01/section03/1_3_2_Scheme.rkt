#lang sicp

(lambda (x) (+ x 4))

(lambda (x) (/ 1.0 (* x (+ x 2))))

; Then our pi-sum procedure can be expressed without defining any auxiliary procedures as

(define (sum term a next b)
  (if (> a b)
      0
      (+ (term a)
         (sum term (next a) next b))))

(define (pi-sum a b)
  (sum (lambda (x) (/ 1.0 (* x (+ x 2))))
       a
       (lambda (x) (+ x 4))
       b))

(* 8 (pi-sum 1 1000))

(define (square x) (* x x))
(define (cube x) (* x (square x))) 

(define (integral f a b dx)
  (* (sum f
          (+ a (/ dx 2.0))
          (lambda (x) (+ x dx))
          b)
     dx))

(integral cube 0 1 0.01)
(integral cube 0 1 0.001)

(define (plus4 x) (+ x 4))
(define plus4-redo (lambda (x) (+ x 4)))


((lambda (x y z) (+ x y (square z))) 1 2 3)

; let to create local variables

; step 1 start with this

(define (f x y)
  (define (f-helper a b)
    (+ (* x (square a))
       (* y b)
       (* a b)))
  (f-helper (+ 1 (* x y)) 
            (- 1 y)))

; step 2 use a lambda

(define (f2 x y)
  ((lambda (a b)
    (+ (* x (square a))
       (* y b)
       (* a b)))
  (+ 1 (* x y))
  (- 1 y)))

; step 3 use a let





