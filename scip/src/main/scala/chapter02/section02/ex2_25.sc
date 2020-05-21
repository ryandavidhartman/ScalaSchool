/* Exercise 2.25

Give combinations of cars and cdrs that will pick 7 from each of the following lists:

(1 3 (5 7) 9)

((7))

(1 (2 (3 (4 (5 (6 7))))))

 */

/*
(define l1 (list 1 3 (list 5 7) 9))
(car (cdr (car (cdr (cdr l1)))))
 */

def car(l: Any): Any = l match {
  case x :: _ => x
}

def cdr(l: Any): Seq[Any] = l match {
  case _ :: xs => xs
  case x => Seq(x)
}


val l1 = Seq(1, 3, Seq(5, 7), 9)
car(cdr(car(cdr(cdr(l1)))))

/*
(define l1 (list (list 7)))
(car (car l1))
 */

val l2 = Seq(Seq(7))
car(car(l2))

val l3 = Seq(1, Seq(2, Seq(3, Seq(4, Seq(5, Seq(6,7))))))
car(
  cdr(
    car(
      cdr(
        car(
          cdr(
            car(
              cdr(
                car(
                  cdr
                    (car
                      (cdr(l3)
                    )
                  )
                )
              )
            )
          )
        )
      )
    )
  )
)
