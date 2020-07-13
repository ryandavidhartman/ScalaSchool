#lang sicp

(define (fold-right op initial sequence)
  (if (null? sequence)
      initial
      (op (car sequence)
          (fold-right op initial (cdr sequence)))))

(define (fold-left op initial sequence)
  (define (iter result rest)
    (if (null? rest)
        result
        (iter (op result (car rest))
              (cdr rest))))
  (iter initial sequence))


(define (reverse sequence)
  (fold-right (lambda (l r) (append r (list l))) nil sequence))


(reverse (list 1 2 3 4 5))


(define (reverse2 sequence)
  (fold-left (lambda (l r) (cons r l)) nil sequence))


(reverse2 (list 1 2 3 4 5))
