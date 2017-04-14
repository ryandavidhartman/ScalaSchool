package observatory

import java.io.File

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import org.scalatest.prop.Checkers

import scala.collection.concurrent.TrieMap

@RunWith(classOf[JUnitRunner])
class InteractionTest extends FunSuite with Checkers {

  import Interaction._

  val station1 = Location(80.0, -170.00)
  val station2 = Location(80.0, 170.00)
  val station3 = Location(-80.0, -170.00)
  val station4 = Location(-80.0, 170.00)
  val station5 = Location(0.00, 0.00)

  val bloomtingtonLoc = Location(39.1653, 86.5264)

  val locAndTemps: Iterable[(Location, Double)] =
    List((station1, -50.0), (station2, -50.0), (station3, -50), (station4, -50),  (station5, 60))

  val colors: Iterable[(Double, Color)] = List(
    (60.0,  Color(255, 255, 255)),
    (32.0,  Color(255, 0,     0)),
    (12.0,  Color(255, 255,   0)),
    (0.0,   Color(0,   255, 255)),
    (-15.0, Color(0,     0, 255)),
    (-27.0, Color(255,   0, 255)),
    (-50.0, Color(33,    0, 107)),
    (-60.0, Color(0,     0,   0))
  )

  test("tiles image zoom 0 should work") {
    val image = tile(locAndTemps, colors, 0, 0, 0)
    assert(image.height == 256)
    assert(image.width === 256)
    //image.output(new java.io.File("/Users/ryan.hartman/Desktop/map0.png"))
  }

  test("tiles zoom 1 should work at 0 0") {
    val image = tile(locAndTemps, colors, 1, 0, 0)
    assert(image.height == 256)
    assert(image.width === 256)
   // image.output(new java.io.File("/Users/ryan.hartman/Desktop/map1-0-0.png"))
  }

  test("tiles zoom 1 should work at 1 0") {
    val image = tile(locAndTemps, colors, 1, 1, 0)
    assert(image.height == 256)
    assert(image.width === 256)
    //image.output(new java.io.File("/Users/ryan.hartman/Desktop/map1-1-0.png"))
  }

  test("tiles zoom 1 should work at 0 1") {
    val image = tile(locAndTemps, colors, 1, 0, 1)
    assert(image.height == 256)
    assert(image.width === 256)
    //image.output(new java.io.File("/Users/ryan.hartman/Desktop/map1-0-1.png"))
  }

  test("tiles zoom 1 should work at 1 1") {
    val image = tile(locAndTemps, colors, 1, 1, 1)
    assert(image.height == 256)
    assert(image.width === 256)
   // image.output(new java.io.File("/Users/ryan.hartman/Desktop/map1-1-1.png"))
  }

  test("figure out tiles for lat 0.0 and lon 0.0") {
    val (x0, y0) = latLonToTile(0.0, 0.0, 0)
    assert(x0 ===  0)
    assert(y0 === 0)

    val (x1, y1) = latLonToTile(0.0, 0.0, 1)
    assert(x1 ===  1)
    assert(y1 === 1)


    val (x2, y2) = latLonToTile(0.0, 0.0, 2)
    assert(x2 ===  2)
    assert(y2 === 2)

    val (x4, y4) = latLonToTile(0.0, 0.0, 4)
    assert(x4 ===  8)
    assert(y4 === 8)

    val (x8, y8) = latLonToTile(0.0, 0.0, 8)
    assert(x8 ===  128)
    assert(y8 === 128)
  }


}
