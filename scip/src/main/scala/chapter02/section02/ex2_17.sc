/*Exercise 2.17.

Define a procedure last-pair that returns the list that contains only the last element of a given (nonempty) list:
  (last-pair (list 23 72 149 34))
(34)
*/

@scala.annotation.tailrec
def last_pair[T](seq: Seq[T]): Seq[T] = seq match {
  case x :: Nil => Seq(x)
  case _ :: xs => last_pair(xs)
}

last_pair(List(23, 72, 149, 34))

