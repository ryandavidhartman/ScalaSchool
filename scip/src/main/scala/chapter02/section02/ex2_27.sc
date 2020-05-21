/* Exercise 2.27

Modify your reverse procedure of exercise 2.18 to produce a deep-reverse procedure that takes a list as argument and
returns as its value the list with its elements reversed and with all sublists deep-reversed as well. For example,

(define x (list (list 1 2) (list 3 4)))

x
((1 2) (3 4))

(reverse x)
((3 4) (1 2))

(deep-reverse x)
((4 3) (2 1))

 */

def reverse[T](seq: Seq[T]): Seq[T] = {

  @scala.annotation.tailrec
  def reverse_helper(s1: Seq[T], s2: Seq[T]): Seq[T] =
    if(s1.nonEmpty)
      reverse_helper(s1.tail, s1.head +: s2)
    else
      s2
  reverse_helper(seq, seq.empty)
}

val x = List(List(1,2), List(3,4))
reverse(x)

def deep_reverse(seq: Seq[Any]): Seq[Any] = {
  reverse(seq).map {
    case xs: Seq[Any] => deep_reverse(xs)
    case y => y
  }
}

deep_reverse(x)


