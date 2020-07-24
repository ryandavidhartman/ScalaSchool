#lang sicp

(define (accumulate op initial sequence)
  (if (null? sequence)
      initial
      (op (car sequence)
          (accumulate op initial (cdr sequence)))))

(define (enumerate-interval i j)
  (if (> i j)
      nil
      (cons i (enumerate-interval (+ i 1) j))))


(define (flatmap proc seq)
  (accumulate append nil (map proc seq)))

(define (filter predicate seq)
  (cond ((null? seq) nil)
         ((predicate (car seq)) (cons (car seq) (filter predicate (cdr seq))))
         (else (filter predicate (cdr seq)))))

(define (correct-sum? seq s)
  (= s (accumulate + 0 seq)))

(define (triples n sum)
  (filter (lambda (p)
            (correct-sum? p sum))
          (flatmap
           (lambda (i)
             (flatmap
              (lambda (j)
                (map (lambda (k) (list i j k))
                     (enumerate-interval 1 n)))
              (enumerate-interval 1 n)))
           (enumerate-interval 1 n))))
           

(triples 6 7)
