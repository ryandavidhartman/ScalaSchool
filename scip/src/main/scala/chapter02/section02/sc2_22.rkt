#lang sicp

(define (square x) (* x x))
 
;(define (square-list items)
;  (define (iter things answer)
;    (if (null? things)
;        answer
;        (iter (cdr things) 
;              (cons (square (car things))
;                    answer))))
;  (iter items nil))

; here is why that is backwards
; (square-list (list 1 2 3 4))
; iter (list 1 2 3 4) nil
; iter (list 2 3 4) (cons (square 1) nil))
; iter (list 2 3 4) (list 1)
; iter (list 3 4) (cons (square 2) (list 1))
; iter (list 3 4) (list 4 1)
; iter (list 4) (cons (square 3) (list 4 1))
; iter (list 4) (list 9 4 1)
; iter nil (cons (square 4) (list 9 4 1))
; ; iter nil (list 16 9 4 1)
; (list 16 9 4 1)


(define (square-list items)
  (define (iter things answer)
    (if (null? things)
        answer
        (iter (cdr things)
              (cons answer
                    (square (car things))))))
  (iter items nil))

; this is also messed up!  Here is why
(square-list (list 1 2 3 4))

;(square-list (list 1 2 3 4))
;iter (list 1 2 3 4) nil
;iter (list 2 3 4) (cons nil (square 1))
;iter (list 2 3 4) ().1  -> we are messed up here we don't have the (list 1) we have cons cell with nil.1
;liter (list 3 4) (().1).4  -> a cons cell with the cons ((cons nil 1) 4)
; etc
