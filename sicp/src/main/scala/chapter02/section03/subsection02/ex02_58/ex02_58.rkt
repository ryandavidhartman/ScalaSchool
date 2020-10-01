#lang sicp

(define variable? symbol?)

(define (same-variable? v1 v2)
  (and (variable? v1)
       (variable? v2)
       (eq? v1 v2)))

(define (=number? exp num)
  (and (number? exp) (= exp num)))

(define (simplify-term x)
  (cond ((variable? x) x)
        ((= (length x) 1) (car x))
        (else x)))

(define (make-sum a1 a2)
  (cond ((=number? a1 0) a2)
        ((=number? a2 0) a1)
        ((and (number? a1)
              (number? a2)) (+ a1 a2))
        (else (list a1 '+ a2))))

(define (make-product m1 m2)
  (cond ((=number? m1 0) 0)
        ((=number? m2 0) 0)
        ((=number? m1 1) m2)
        ((=number? m2 1) m1)
        ((and (number? m1) (number? m2)) (* m1 m2))
        (else (list m1 '* m2))))

(define (make-exponentiation base exponent)
  (cond ((=number? exponent 0) 1)
        ((=number? exponent 1) base)
        ((=number? base 0) 0)
        (else (list base '** exponent))))

(define (sum? x)
  (and (pair? x) (memq '+ x)))
 
(define (simple-sum? x)
  (and (sum? x) (null? (cdddr x))))

(define (product? x)
  (and (pair? x)
       (not (sum? x))
       (memq '* x)))
 
(define (simple-product? x)
  (and (product? x) (null? (cdddr x))))

(define (exponentiation? x)
  (and (pair? x)
       (not (sum? x))
       (not (product? x))
       (memq '** x)))


(define (addend expr) 
   (define (iter expr result) 
         (if (eq? (car expr) '+) 
           result 
           (iter (cdr expr) (append result (list (car expr)))))) 
   (let ((result (iter expr '()))) 
     (simplify-term result))) 

(define (augend expr) 
   (let ((result (cdr (memq '+ expr)))) 
     (simplify-term result))) 
 

 (define (multiplier expr) 
   (define (iter expr result) 
         (if (eq? (car expr) '*) 
           result 
           (iter (cdr expr) (append result (list (car expr)))))) 
   (let ((result (iter expr '()))) 
     (simplify-term result))) 

(define (multiplicand expr) 
   (let ((result (cdr (memq '* expr)))) 
     (simplify-term result)))

(define (base x)
  (car x))

(define (exponent x)
  (simplify-term (cddr x)))

(define (deriv exp var)
  (cond ((number? exp) 0)
        ((variable? exp)
         (if (same-variable? exp var) 1 0))
        ((sum? exp)
         (make-sum (deriv (addend exp) var)
                   (deriv (augend exp) var)))
        ((product? exp)
         (make-sum
          (make-product (multiplier exp)
                        (deriv (multiplicand exp) var))
          (make-product (deriv (multiplier exp) var)
                        (multiplicand exp))))
        ((exponentiation? exp)
         (let ((b (base exp))
               (e (exponent exp)))
           (make-product e
                         (make-product (make-exponentiation b (make-sum e -1))
                                       (deriv b var)))))
        (else
         (error "unknown expression type -- DERIV" exp))))
 


 
(deriv '(x + 3 * (x + y + 2)) 'x)
4
 
(deriv '(x + 3 * (x + y + 2)) 'y)
3
 
(deriv '(x * y + y * x) 'x)
'(y + y)
 
; should still support part a) style nested expressions
(deriv '(x ** (3 * (x + (y + 2)))) 'x)
'((3 * (x + (y + 2))) * (x ** ((3 * (x + (y + 2))) + -1)))
 
(deriv '(x + (3 * (x ** (y + 2)))) 'x)
'(1 + (3 * ((y + 2) * (x ** ((y + 2) + -1)))))
 
(deriv '(x + (3 * (x + (y + 2)))) 'x)
4
(deriv '(x + (3 * (x ** (y + 2)))) 'x)
'(1 + (3 * ((y + 2) * (x ** ((y + 2) + -1)))))
