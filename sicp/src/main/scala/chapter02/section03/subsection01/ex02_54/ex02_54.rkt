#lang sicp

(define (equal? a b)
  (cond ((and (pair? a) (pair? b))
           (and (equal? (car a) (car b)) (equal? (cdr a) (cdr b))))
        ((and (not (pair? a)) (not (pair? b)))
            (eq? a b))
        (else false)))  
        
        

(equal? '(0 1 2) '(0 1 2))
(equal? '(0 1 2) '(9 9 9))
(equal? 5 6)

(equal? '(a b c) '(a b c))
(equal? '(a b) '(a b c))
(equal? 5 6)
(equal? '((a) b c) '((a) b c))