#lang sicp

(car ''abracadabra)
(cdr ''abracadabra)

(car '(quote abracadabra))
(cdr '(quote abracadabra))


(car (quote (quote abracadabra)))
(cdr (quote (quote abracadabra)))

(car (list 'quote 'abracadabra))
(cdr (list 'quote 'abracadabra))


