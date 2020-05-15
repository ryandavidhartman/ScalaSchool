#lang sicp

(define (square-list items)
  (if (null? items)
      nil
      (let ([h (car items)]
            [t (cdr items)])
      (cons (* h h) (square-list t)))))

;(define (square-list items)
;  (map (lambda (x) (* x x)) items))

(square-list (list 1 2 3 4))