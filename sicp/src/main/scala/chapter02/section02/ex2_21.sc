/* Exercise 2.21

The procedure square-list takes a list of numbers as argument and returns a list of the squares of those numbers.

(square-list (list 1 2 3 4))
(1 4 9 16)

Here are two different definitions of square-list. Complete both of them by filling in the missing expressions:

(define (square-list items)
  (if (null? items)
      nil
      (cons <??> <??>)))
(define (square-list items)
  (map <??> <??>))
 */

def square_list1(l: Seq[Int]): Seq[Int] = {
  if(l.isEmpty)
    Nil
  else {
    val h = l.head
    val t = l.tail
    (h*h) +: square_list1(t)
  }
}

def square_list2(l: Seq[Int]): Seq[Int] = l.map(i => i*i)


square_list1(List(1, 2, 3, 4))
square_list2(List(1, 2, 3, 4))
