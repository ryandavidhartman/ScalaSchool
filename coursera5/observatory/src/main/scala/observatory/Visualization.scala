package observatory

import com.sksamuel.scrimage.{Image, Pixel}
import math._

/**
  * 2nd milestone: basic visualization
  */
object Visualization {

  import SparkSessionWrapper._
  import Constants._



  /**
    * @param temperatures Known temperatures: pairs containing a location and the temperature at this location
    * @param location Location where to predict the temperature
    * @return The predicted temperature at `location`
    */
  def predictTemperature(temperatures: Iterable[(Location, Double)], location: Location): Double = {


    val tempRdd  = spark.sparkContext.parallelize(temperatures.toList).cache()
    val foundLocationTemp = tempRdd.lookup(location)
    if(foundLocationTemp.headOption.isDefined) {
      foundLocationTemp.head
    } else {
      //val sum = tempRdd.map{case (l,_) => 1.0/distanceRaised(l, location, 2)}.sum
      //val weightedTemp = tempRdd.map{case (l,t) => t/distanceRaised(l, location, 2)}.sum

      val bob = tempRdd.map{case (l,t) => (l,t,1.0/distanceRaised(l,location,2))}.map{case (x,y,z) => (y*z,z)}.reduce( (x,y) => (x._1 + y._1, x._2 + y._2))
      bob._1/bob._2
    }
  }



  /**
    * @param points Pairs containing a value and its associated color
    * @param value The value to interpolate
    * @return The color that corresponds to `value`, according to the color scale defined by `points`
    */
  def interpolateColor(points: Iterable[(Double, Color)], value: Double): Color = {
    val colors = points.toList.sortBy(_._1)

    if(value <= colors.head._1) {
      colors.head._2
    }
    else if(value >= colors.last._1) {
      colors.last._2
    }
    else {
      val index = colors.indexWhere(_._1 >= value)
      val delta = (value - colors(index-1)._1) / (colors(index)._1 - colors(index-1)._1)

      val red   = colors(index-1)._2.red   + (colors(index)._2.red   - colors(index-1)._2.red)*delta
      val green = colors(index-1)._2.green + (colors(index)._2.green - colors(index-1)._2.green)*delta
      val blue  = colors(index-1)._2.blue  + (colors(index)._2.blue  - colors(index-1)._2.blue)*delta

      Color(math.round(red).toInt, math.round(green).toInt, math.round(blue).toInt)
    }
  }

  /**
    * @param temperatures Known temperatures
    * @param colors Color scale
    * @return A 360×180 image where each pixel shows the predicted temperature at its location
    */
  def visualize(temperatures: Iterable[(Location, Double)], colors: Iterable[(Double, Color)]): Image = {
    ???
  }

  def distance(loc1: Location, loc2: Location)={

    val lat1:Double = loc1.lat
    val lon1:Double = loc1.lon
    val lat2:Double = loc2.lat
    val lon2:Double = loc2.lon

    val dLat=(lat2 - lat1).toRadians
    val dLon=(lon2 - lon1).toRadians

    val a = pow(sin(dLat/2),2) + pow(sin(dLon/2),2) * cos(lat1.toRadians) * cos(lat2.toRadians)
    val c = 2 * asin(sqrt(a))
    RADIUS_OF_EARTH * c
  }

  def distanceRaised(loc1: Location, loc2:Location, rho: Int): Double = {
    pow(distance(loc1, loc2), rho)
  }

}

