package observatory

import com.sksamuel.scrimage.{Image, Pixel}
import observatory.Visualization.{interpolateColor, pixelPositionToLocation, predictTemperature}

import scala.math._

/**
  * 3rd milestone: interactive visualization
  */
object Interaction {

  /**
    * @param zoom Zoom level
    * @param x X coordinate
    * @param y Y coordinate
    * @return The latitude and longitude of the top-left corner of the tile, as per http://wiki.openstreetmap.org/wiki/Slippy_map_tilenames
    */
  def tileLocation(zoom: Int, x: Int, y: Int): Location = {
    val lat = toDegrees(atan(sinh(Pi * (1.0 - 2.0 * y.toDouble / (1<<zoom)))))
    val lon = x.toDouble / (1<<zoom) * 360.0 - 180.0
    Location(lat, lon)
  }

  def latLonToTile(lat: Double, lon: Double, zoom: Int): (Int, Int) = {
    val x = ((lon + 180.0) / 360.0 * (1<<zoom)).toInt
    val y = ((1 - log(tan(toRadians(lat)) + 1 / cos(toRadians(lat))) / Pi) / 2.0 * (1<<zoom)).toInt

    (x,y)
  }

  /**
    * @param temperatures Known temperatures
    * @param colors Color scale
    * @param zoom Zoom level
    * @param x X coordinate
    * @param y Y coordinate
    * @return A 256×256 image showing the contents of the tile defined by `x`, `y` and `zooms`
    */
  def tile(temperatures: Iterable[(Location, Double)], colors: Iterable[(Double, Color)], zoom: Int, x: Int, y: Int): Image = {
    val pixelArray = scala.collection.mutable.ArrayBuffer[Pixel]()

    val upper_Left = tileLocation(zoom, x, y)
    val delta_lat  = if(zoom == 0 || zoom == 1)
      170.1022/(256*(1+zoom))
    else {
      (upper_Left.lat - tileLocation(zoom, x, y+1).lat)/256
    }

    val delta_long = 360.0/(256*(1<<zoom))

    for(y_iter <- (0 until 256)) {
      for(x_iter <- (0 until 256)) {
        val location = Location(lat = upper_Left.lat + delta_lat*y_iter, lon = upper_Left.lon + delta_long*x_iter)
        val color = interpolateColor(colors, predictTemperature(temperatures, location))
        val pixel = Pixel(color.red, color.green, color.blue, 127);
        pixelArray += pixel
      }
    }

    Image(256,256, pixelArray.toArray)
  }


  /**
    * Generates all the tiles for zoom levels 0 to 3 (included), for all the given years.
    * @param yearlyData Sequence of (year, data), where `data` is some data associated with
    *                   `year`. The type of `data` can be anything.
    * @param generateImage Function that generates an image given a year, a zoom level, the x and
    *                      y coordinates of the tile and the data to build the image from
    */
  def generateTiles[Data](
    yearlyData: Iterable[(Int, Data)],
    generateImage: (Int, Int, Int, Int, Data) => Unit): Unit = {

    def generateTilesForYear(year:Int, data: Data): Unit = {
      for(zoom <- (0 to 3))
        for(x <- (0 until (1<<zoom)))
          for(y <- (0 until (1<<zoom)))
            {
              generateImage(year, zoom, x, y, data)
            }

      yearlyData.foreach{ case (year, data) => generateTilesForYear(year, data)}
    }
  }



}
