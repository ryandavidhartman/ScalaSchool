#lang sicp

(define (variable? x) (symbol? x))

(define (same-variable? v1 v2)
  (and (variable? v1) (variable? v2) (eq? v1 v2)))

(define (=number? exp num)
  (and (number? exp) (= exp num)))

;(define (make-sum a1 a2) (list '+ a1 a2))

;(define (make-product m1 m2) (list '* m1 m2))

(define (make-sum a1 a2)
  (cond ((=number? a1 0) a2)
        ((=number? a2 0) a1)
        ((and (number? a1) (number? a2)) (+ a1 a2))
        (else (list '+ a1 a2))))

(define (make-product m1 m2)
  (cond ((or (=number? m1 0) (=number? m2 0)) 0)
        ((=number? m1 1) m2)
        ((=number? m2 1) m1)
        ((and (number? m1) (number? m2)) (* m1 m2))
        (else (list '* m1 m2))))

(define (make-exponentiation b e)
  (cond ((=number? e 0) 1)
        ((=number? e 1) b)
        ((=number? b 0) 0)
        (else (list '** b e))))

(define (sum? x)
  (and (pair? x) (eq? (car x) '+)))

(define (product? x)
  (and (pair? x) (eq? (car x) '*)))

(define (exponentiation? x)
  (and (pair? x) (eq? (car x) '**)))

(define (addend s) (if (sum? s) (cadr s) (error "not a sum")))
(define (augend s) (if (sum? s) (caddr s) (error "not a sum")))

(define (multiplier p) (if (product? p)(cadr p) (error "not a product")))
(define (multiplicand p) (if (product? p) (caddr p) (error "not a product")))

(define (base e) (if (exponentiation? e)(cadr e) (error "not an exponentiation")))
(define (exponent e) (if (exponentiation? e)(caddr e) (error "not an exponentiation")))


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
          (make-product (exponent exp)
                        (make-product (make-exponentiation (base exp) (make-sum (exponent exp) -1))
                                      (deriv (base exp) var))))
        (else
         (error "unknown expression type -- DERIV" exp))))

; x+3
(deriv '(+ x 3) 'x)

; xy
(deriv '(* x y) 'x)

; xy(x+3)
(deriv '(* (* x y) (+ x 3)) 'x)

;;  (yx)^6
(deriv '(** (* y x) 6) 'x)

;; x^5 +2*x^2 + 5x +1
(deriv '(+ (** x 5) (* 2 (** x 2)) (* 5 x) 1) 'x)
(deriv '(+ (** x 5) (* 2 (** x 2))) 'x)

