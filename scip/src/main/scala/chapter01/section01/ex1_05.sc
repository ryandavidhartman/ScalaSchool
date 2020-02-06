/*
Exercise 1.5.  Ben Bitdiddle has invented a test to determine whether the interpreter he is faced with is using
applicative-order evaluation or normal-order evaluation. He defines the following two procedures:

(define (p) (p))

(define (test x y)
  (if (= x 0)
      0
      y))

Then he evaluates the expression

(test 0 (p))

What behavior will Ben observe with an interpreter that uses applicative-order evaluation?
What behavior will he observe with an interpreter that uses normal-order evaluation? Explain your answer.

(Assume that the evaluation rule for the special form if is the same whether the interpreter is using normal or
applicative order: The predicate expression is evaluated first, and the result determines whether to evaluate the
consequent or the alternative expression.)

Answer:
Scheme is an applicative-order language, namely, that all the arguments to Scheme procedures are evaluated when the
procedure is applied. In contrast, normal-order languages delay evaluation of procedure arguments until the actual
argument values are needed.

So (test 0 (p)) blows up in Scheme

 */

@scala.annotation.tailrec
def p():Int = p()
// NOTE  p()  => will blow up!!


def test1(x: Int, y: Int): Int = {
 if(x == 0) 0 else y
}

// val test1Results = test1(0, p()) will blow up!

def test2(x: Int, y: () => Int): Int = {
  if(x == 0) 0 else y()
}
val test2Results = test2(0, p)  // fine

def test3(x: Int, y: => Int): Int = {
  if(x == 0) 0 else y
}
val test3Results = test3(0, p())



