#lang sicp

; Points
(define (make_point x y) (cons x y))
(define (x_point p) (car p))
(define (y_point p) (cdr p))


; Rectangle Rep 1 define a rectangle in terms of the points for the top left, and bottom right corners

;(define (make_rectangle p1 p2) (cons p1 p2))
;(define (get_topleft r) (car r))
;(define (get_bottomright r) (cdr r))
;(define (get_width r) (abs (- (x_point (get_topleft r)) (x_point (get_bottomright r)))))
;(define (get_height r) (abs (- (y_point (get_topleft r)) (y_point (get_bottomright r)))))

;(define r1 (make_rectangle (make_point 2 2) (make_point -1 -1)))


; Rectangle Rep 2 define a rectangle in terms of height and width

(define (make_rectangle h w) (cons h w))
(define (get_height r) (car r))
(define (get_width r) (cdr r))

(define r1 (make_rectangle 3 3))




; These functions don't change

(define (rectangle_perimeter r) (* 2 (+ (get_width r) (get_height r))))
(define (rectangle_area r) (* (get_width r) (get_height r)))

(rectangle_area r1)
(rectangle_perimeter r1)


