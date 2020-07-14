#lang racket
(require sdraw)
(require pict
         pict/tree-layout)

(define (draw tree)
  (define (viz tree)
    (cond
      ((null? tree) #f)
      ((not (pair? tree))
       (tree-layout #:pict (cc-superimpose
                            (disk 30 #:color "white")
                            (text (symbol->string tree)))))
      ((not (pair? (car tree)))
       (apply tree-layout (map viz (cdr tree))
              #:pict (cc-superimpose
                      (disk 30 #:color "white")
                      (text (symbol->string (car tree))))))))

    (if (null? tree)
      #f
      (naive-layered (viz tree))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;                                                                 ;
;                             Lecture start                       ;                            
;                                                                 ;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

; ((1 2) 3 4) can be constructed with:
(cons (list 1 2) (list 3 4))


(define tree1 (cons (list 1 2) (list 3 4)))
(sdraw tree1 #:null-style '/)

(cons (list 1 2) (list 3 4))
(define tree2 '(|| (|| |1| |2|) |3| |4|))
(draw tree2)

(length tree1)

(define (count-leaves x)
  (cond ((null? x) 0)
        ((not (pair? x)) 1)
        (else (+ (count-leaves (car x))
                 (count-leaves (cdr x))))))

(count-leaves tree1)