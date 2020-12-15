package section4

object Options extends App {

  val myFirstOption: Option[Int] = Some(4)
  val mySecondOption: Option[Int] = None

  println(s"Two options: $myFirstOption and $mySecondOption")

}