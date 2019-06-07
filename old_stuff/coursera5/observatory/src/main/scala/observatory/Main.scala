package observatory

import java.io.File

import observatory.Interaction.tile

object Main extends App {

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



    import Extraction._
    val tempsFor2015: Iterable[(Location, Double)] = locationYearlyAverageRecords(locateTemperatures(year = 2015, stationsFile = "/stations.csv", temperaturesFile = "/2015.csv"))

    for(x <-(0 to 7))
      for(y <- (0 to 7)) {
        val image = tile(tempsFor2015, colors, 3, x, y)
        val path = s"/Users/ryan.hartman/Desktop/bob/${x}-${y}.png"
        System.out.println("Writing: " + path )
        image.output(new File(path))
      }
}
