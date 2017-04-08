package observatory

case class Location(lat: Double, lon: Double)

case class Color(red: Int, green: Int, blue: Int)

object Constants {
  val NO_DATA:Double = 0.0
  val RADIUS_OF_EARTH = 6372.8
}

