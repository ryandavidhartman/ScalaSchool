/*
Exercise 2.18.  Define a procedure reverse that takes a list as argument and returns a list of the same elements in reverse order:
  (reverse (list 1 4 9 16 25))
(25 16 9 4 1)
*/

//let do a classic cons/cdr implementation

def reverse[T](seq: Seq[T]): Seq[T] = {

  @scala.annotation.tailrec
  def reverse_helper(s1: Seq[T], s2: Seq[T]): Seq[T] =
    if(s1.nonEmpty)
      reverse_helper(s1.tail, s1.head +: s2)
    else
      s2
  reverse_helper(seq, seq.empty)
}

reverse (List(1, 4, 9, 16, 25))
