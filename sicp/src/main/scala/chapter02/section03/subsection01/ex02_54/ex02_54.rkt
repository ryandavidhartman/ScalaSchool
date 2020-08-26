#lang sicp

(define (equal? a b)
  (cond ((and (null? a) (null? b)) #t)
        ((or (null? a) (null? b)) #f)
        ((eq? (car a) (car b)) (equal? (cdr a) (cdr b)))
        (else #f)))
     
(equal? '(0 1 2) '(0 1 2))
(equal? '(0 1 2) '(9 9 9))