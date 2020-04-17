#lang sicp

; Procedure 1 computes the sum of the integers from a through b
(define (sum-integers-1 a b)
  (if (> a b)
      0
      (+ a (sum-integers-1 (+ a 1) b))))

(sum-integers-1 1 4)

; Procedure 2 computes the sum of the cubes integers from a through b

(define (cube x) (* x x x))

(define (sum-cubes-1 a b)
  (if (> a b)
      0
      (+ (cube a) (sum-cubes-1 (+ a 1) b))))

(sum-cubes-1 1 4)

; Procedures 3 computes pi with this series:
; 1/(1*3) + 1/(5*7) + 1/(9*11) + 1/(13*15) + ...  = pi/8

(define (pi-sum-1 a b)
  (if (> a b)
      0
      (+ (/ 1.0 (* a (+ a 2))) (pi-sum-1 (+ a 4) b))))

(* 8 (pi-sum-1 1 1000000))

; Procedures 1 to 3 can be generalized in the following form:


(define (sum term a next b)
  (if (> a b)
      0
      (+ (term a)
         (sum term (next a) next b))))

; Procedure 1 with the new abstraction

(define (inc x) (+ x 1)) 

(define (sum-integers a b)
  (sum identity a inc b))

(sum-integers 1 3)

; Prodecure 2 with the new abstraction
(define (sum-cubes a b)
  ( sum cube a inc b))
(sum-cubes 1 4)

; Prodecure 3 with the new abstraction

(define (pi-sum a b)
  (define (pi-term x)
    (/ 1.0 (* x (+ x 2))))
  (define (pi-next x)
    (+ x 4))
  (sum pi-term a pi-next b))

(* 8 (pi-sum 1 1000))


; We can use our sum prodecure to model integration:

(define (integral f a b dx)
  (define (add-dx x) (+ x dx))
  (* (sum f (+ a (/ dx 2.0)) add-dx b)
     dx))

(integral cube 0 1 0.01)
(integral cube 0 1 0.001)