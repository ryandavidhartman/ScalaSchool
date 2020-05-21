#lang racket
(require sdraw)
(require pict
         pict/tree-layout)


(list 1 (list 2 (list 3)))

(sdraw (list 1 (list 2 (list 3))) #:null-style '/)



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

(define t1 '(\. (\1) (\. \2 \3)))
(draw t1)
