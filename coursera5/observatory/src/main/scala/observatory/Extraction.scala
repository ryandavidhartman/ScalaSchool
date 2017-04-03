package observatory

import java.time.LocalDate
import java.nio.file.Paths

import org.apache.spark.rdd.RDD

/**
  * 1st milestone: data extraction
  */
object Extraction {

  import SparkSessionWrapper._

  val NO_DATA:Double = -999.99

  /**
    * @param year             Year number
    * @param stationsFile     Path of the stations resource file to use (e.g. "/stations.csv")
    * @param temperaturesFile Path of the temperatures resource file to use (e.g. "/1975.csv")
    * @return A sequence containing triplets (date, location, temperature)
    */
  def locateTemperatures(year: Int, stationsFile: String, temperaturesFile: String): Iterable[(LocalDate, Location, Double)] = {
    val stationsFilePath = fsPath(stationsFile)
    val stationsRdd:RDD[Station] = convertRawRddToStationsRdd(spark.sparkContext.textFile(stationsFilePath))

    val temperaturesFilePath = fsPath(temperaturesFile)
    val temperaturesRdd = spark.sparkContext.textFile(temperaturesFilePath)
    ???
  }

  def fsPath(resource: String): String =
    Paths.get(getClass.getResource(resource).toURI).toString


  /**
    * @param records A sequence containing triplets (date, location, temperature)
    * @return A sequence containing, for each location, the average temperature over the year.
    */
  def locationYearlyAverageRecords(records: Iterable[(LocalDate, Location, Double)]): Iterable[(Location, Double)] = {
    ???
  }

  def convertRawRddToStationsRdd(lines: RDD[String]): RDD[Station] = {
    lines.map(line => {
      line.split(",") match {
        case a if (a.length == 4) => Station(id = s"""${a(0)}-${a(1)}""", latitude = safeToDouble(a(2)), longitude = safeToDouble(a(3)))
        case a if (a.length == 3) => Station(id = s"""${a(0)}-""",        latitude = safeToDouble(a(1)), longitude = safeToDouble(a(2)))
        case a if (a.length == 2) => Station(id = s"""${a(0)}-${a(1)}""")
        case a if (a.length == 1) => Station(id = s"""${a(0)}-""")
        case _ => throw new Exception("line: " + line)
      }
    })
  }

  private def safeToDouble(v: String): Double = if(!v.isEmpty) v.toDouble else NO_DATA

  case class Station(id: String, latitude: Double = NO_DATA, longitude: Double = NO_DATA)
  case class Temperature(id: String, month: Int, day: Int, temp: Double)

}


