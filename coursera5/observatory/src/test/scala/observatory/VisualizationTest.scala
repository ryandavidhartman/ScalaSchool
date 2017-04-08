package observatory


import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import org.scalatest.prop.Checkers

@RunWith(classOf[JUnitRunner])
class VisualizationTest extends FunSuite with Checkers {

  import Visualization._
  import Constants._

  test("distance calc") {
    assert(Math.abs(distance(Location(90.0, 0.0), Location(-90.0,   0.0)) - RADIUS_OF_EARTH*Math.PI)   < 0.0001)
    assert(Math.abs(distance(Location(90.0, 0.0), Location(-00.0,   0.0)) - RADIUS_OF_EARTH*Math.PI/2) < 0.0001)
    assert(Math.abs(distance(Location(00.0, 0.0), Location(0.0, 180.0))   - RADIUS_OF_EARTH*Math.PI)   < 0.0001)
  }

  test("predictTemperature when location is in array") {
    val indyLoc = Location(39.7684, 86.15181)
    val denverLoc = Location(39.7392, 104.9902)
    val londonLoc = Location(51.5074, 0.1278)
    val madridLoc = Location(40.4168, 3.7038)

    val locAndtemps:Iterable[(Location, Double)] = List((indyLoc, 10.0), (denverLoc, 15.0), (londonLoc, 5.0), (madridLoc, 20))

    assert(predictTemperature(locAndtemps, indyLoc) === 10.0)
    assert(predictTemperature(locAndtemps, denverLoc) === 15.0)
    assert(predictTemperature(locAndtemps, londonLoc) === 5.0)
    assert(predictTemperature(locAndtemps, madridLoc) === 20.0)
  }

  test("predictTemperature when location is not in array") {
    val indyLoc = Location(39.7684, 86.15181)
    val denverLoc = Location(39.7392, 104.9902)
    val londonLoc = Location(51.5074, 0.1278)
    val madridLoc = Location(40.4168, 3.7038)

    val bloomtingtonLoc = Location(39.1653, 86.5264)

    val locAndtemps: Iterable[(Location, Double)] = List((indyLoc, 10.0), (denverLoc, 15.0), (londonLoc, 5.0), (madridLoc, 20))

    val predictedBloomingtonTemp = predictTemperature(locAndtemps, bloomtingtonLoc)

    assert(predictedBloomingtonTemp > 10.0 && predictedBloomingtonTemp < 11.0)
  }

  test("predict colors") {

    val scale = List((0.0,Color(255,0,0)), (1.0,Color(0,0,255)))

    val predicated = interpolateColor(scale, 0.25)

    assert(predicated === Color(191,0,64))

  }


}
