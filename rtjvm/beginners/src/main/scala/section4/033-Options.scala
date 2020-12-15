package section4

object Options extends App {

  val myFirstOption: Option[Int] = Some(4)
  val mySecondOption: Option[Int] = None

  println(s"Two options: $myFirstOption and $mySecondOption")

  // Using Options to protect us from unsafe APIs

  def unsafeMethod(): String = null  // Say we have this bad API that sometimes returns a null value

  val result = Option(unsafeMethod())  // Now we are protected!  Note DO NOT DO val result = Some(unsafeMethod())
  println(result)

  //How do we use this kind of optional result?
  // One way:  say we have a backup method return a concrete value
  def backupMethod(): String = "A backup result"
  val result2: String = Option(unsafeMethod()).getOrElse(backupMethod())

  // Now if you are writing an API you should return Options if your API might fail.

  def betterUnsafeMethod(): Option[String] = None
  def betterBackupMethod: String = "A backup result"
  val result3 = betterUnsafeMethod().getOrElse(betterBackupMethod)

  // A second way is with "chained" options
  val result5 = Option(unsafeMethod()).orElse(Some(backupMethod()))

  // or
  def betterBackupMethod2(): Option[String] = Some("backup")
  val result6 = betterUnsafeMethod() orElse betterBackupMethod2()

  //
  // functions on Options
  //

  // isEmpty
  println(myFirstOption.isEmpty)

  // get
  // An unsafe way to get a value out of an option!  It will get the value or throw a NPE
  println(myFirstOption.get)

  // OUR FAVORITES: map, flatMap, and filter

  val emptyGuy: Option[Int] = None
  val nonemptyGuy: Option[Int] = Option(22)

  println(s"Mapping over an empty option: ${emptyGuy.map(_ * 2)}")
  println(s"Mapping over an non-empty option: ${nonemptyGuy.map(_ * 2)}")

  println(s"Filtering over an empty option: ${emptyGuy.filter(_ % 2 != 0)}")
  println(s"Filter over an non-empty option: ${nonemptyGuy.filter(_ % 2 != 0)}")
  println(s"Filter over an non-empty option again: ${nonemptyGuy.filter(_ % 2 == 0)}")

  def someCalc(i: Int): Option[Int] = Some(i*10)  // some function that goes from Int to Option[Int]

  println(s"Flat Mapping over an empty option: ${emptyGuy.flatMap(someCalc(_))}")
  println(s"Mapping over an non-empty option: ${nonemptyGuy.flatMap(someCalc(_))}")

  // Now since Options have map, flatMap and filter we can do for-comprehensions!!!




}