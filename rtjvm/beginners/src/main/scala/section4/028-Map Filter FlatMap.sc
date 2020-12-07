/*
  Does our Generic List 6 support for comprehensions???
*/

import section4.GenericList6._

val nums = 1 +: 2 +: 3 +: 4+: Empty6
val letters = 'a' +: 'b' +: 'c' +: 'd' +: Empty6

val results = for {
  l <- letters
  n <- nums
} yield s"$l$n"

// for just WORKS because MyList6 has map, flatMap and filter!!!


