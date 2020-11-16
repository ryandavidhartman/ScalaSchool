def getZipsForCities(cityName: String): List[Int] = cityName.toLowerCase.trim match {
  case "indianapolis" => List(46205, 46206, 46207, 46208)
  case "fort wayne" =>   List(46804, 46805, 46806, 46807)
  case "evansville" =>   List(47705, 47706, 47708, 47710)
  case "south bend" =>   List(46613, 46615, 46616, 46617)
  case "fishers" =>      List(46037, 46038, 46040, 46085)
  case "carmel" =>       List(46032, 46033, 46074, 46082)
  case "bloomington" =>  List(47401, 47403, 47405, 47406)
  case "hammond" =>      List(46320, 46323, 46324, 46325)
  case _ =>              List.empty[Int]
}

val cities: List[String] = List("Indianapolis", "Fort Wayne")

// mutable variable way
var zip1: List[Int] = List.empty[Int]
cities.foreach{c =>
  zip1 = zip1 ++ getZipsForCities(c)
}
zip1.length

// what you want to do!
val zips1 = cities.map(getZipsForCities)

// this is the works
val zips2 = cities.flatMap(getZipsForCities)


// here is a little case study:

abstract class MyOption[+T] {
  def get(): T
  def map[U](f: T => U): MyOption[U]
  def flatMap[U](f: T => MyOption[U]): MyOption[U]
}

case object MyNone extends MyOption[Nothing] {
  def get() = throw new NoSuchElementException("Nothing to get!")
  def map[U](f: Nothing => U): MyOption[U] = MyNone
  def flatMap[U](f: Nothing => MyOption[U]): MyOption[U] = MyNone
}

class MySome[+T](data: T) extends MyOption[T] {
  def get(): T = data
  def map[U](f: T => U): MyOption[U] = new MySome(f(data))
  def flatMap[U](f: T => MyOption[U]): MyOption[U] = f(data)
}

val maybeInt: MyOption[Int] = new MySome(5)
val getInt: Int = maybeInt.get()
val maybeString: MyOption[String] = maybeInt.map(i => s"Int $i now a string")
val getString = maybeString.get()

def getBigNumbers(i: Int): MyOption[Int] = {
  if(i > 1000)
    new MySome(i)
  else MyNone
}

val maybeBig1 = maybeInt.flatMap(getBigNumbers)
val maybeInt2 = new MySome(1001)
val maybeBig2 = maybeInt2.flatMap(getBigNumbers)
