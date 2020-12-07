package section4

object MapFlatMapFilterFor extends  App {

  //Finally using list from the standard library
  val list = List(1,2,3)
  println(list)
  println(list.head)
  println(list.tail)

  // map exists, just like the map we made in our Generic List!
  println(list.map(_ + 1))
  println(list.map(_ + " is a number"))


  //Here are some syntax variations of map (similar stuff is possible for flatMap
  val syntax1 = list.map { x =>
    x * 2
  }

  val syntax2 = list.map(x => x *2)

  val syntax3 = list.map(_ * 2)

  assert(syntax1 == syntax2 && syntax1 == syntax3)


  // filter exists, just like the filter we made in our Generic List!
  println(list.filter(_ % 2 == 0))

  // flapMap exists, just like the filter we made in our Generic List!
  val toPair = (x: Int) => List(x, x+1)
  println(list.flatMap(toPair))

  /*
  Exercise print all possible combinations of two lists.
  e.g.  given List(1,2,3,4) and List('a', 'b', 'c', 'd')
  print -> "a1", "a2", "a3" ...  "d4"
   */

  val nums = List(1,2,3,4)
  val letters = List('a', 'b', 'c', 'd')

  val results1 = letters.flatMap(l => nums.map(n => s"$l$n"))

  val results2 = for {
    l <- letters
    n <- nums
  } yield s"$l$n"

  //The formulations for results1 and results2 are equivalent!
  assert(results1 == results2)
  println(results1)


  // To the same with another list of say colors = List("black", "white")
  val colors = List("black", "white")
  val results3 = letters.flatMap(l => nums.flatMap(n => colors.map(c => s"$l$n$c")))

  // OR with a for comprehension:

  val results4 = for {
    l <- letters
    n <- nums
    c <- colors
  } yield s"$l$n$c"

  assert(results3 == results4)
  println(results3)

  // foreach exists, just like the filter we made in our Generic List!
  list.foreach(println)

  // We have seen how the for comprehension is just sugar for map and flatMap.  But
  // it supports filter too.

  val results5 = letters.flatMap(l => nums.filter(_ % 2 == 0).flatMap(n => colors.map(c => s"$l$n$c")))

  val results6 = for {
    l <- letters
    n <- nums if n % 2 == 0  // you can add a filter too!
    c <- colors
  } yield s"$l$n$c"

  assert(results5 == results6)
  println(results5)

  // You can also use a for comprehension, as a foreach, just remove the "yield"

  for {
    n <- nums
  } println(n)

}

