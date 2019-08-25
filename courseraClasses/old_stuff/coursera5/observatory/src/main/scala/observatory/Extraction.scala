package observatory

import java.time.LocalDate
import java.nio.file.Paths

import org.apache.spark.rdd.RDD

/**
  * 1st milestone: data extraction
  */
object Extraction {

  import SparkSessionWrapper._
  import Constants._

  /**
    * @param year             Year number
    * @param stationsFile     Path of the stations resource file to use (e.g. "/stations.csv")
    * @param temperaturesFile Path of the temperatures resource file to use (e.g. "/1975.csv")
    * @return A sequence containing triplets (date, location, temperature)
    */
  def locateTemperatures(year: Int, stationsFile: String, temperaturesFile: String): Iterable[(LocalDate, Location, Double)] = {
    val stationsFilePath = fsPath(stationsFile)
    val stationsRdd:RDD[(String, Location)] = convertRawRddToStationsRdd(spark.sparkContext.textFile(stationsFilePath)).filter{case (id, loc) => loc.lat != NO_DATA && loc.lon != NO_DATA}.persist

    val temperaturesFilePath = fsPath(temperaturesFile)
    val temperaturesRdd:RDD[(String, (LocalDate, Double))] = convertRawRddtoTemperaturesRdd(spark.sparkContext.textFile(temperaturesFilePath), year).persist()

    stationsRdd.join(temperaturesRdd).map{ case (string, (loc, (date, temp))) => (date, loc, temp)}.collect()

  }

  def fsPath(resource: String): String =
    Paths.get(getClass.getResource(resource).toURI).toString


  /**
    * @param records A sequence containing triplets (date, location, temperature)
    * @return A sequence containing, for each location, the average temperature over the year.
    */
  def locationYearlyAverageRecords(records: Iterable[(LocalDate, Location, Double)]): Iterable[(Location, Double)] = {
    val locationAndTemps:RDD[(Location, (Double, Int))] = spark.sparkContext.parallelize(records.map{case (_, l, t) => (l,(t,1))}.toList)
    locationAndTemps.reduceByKey{case ((t1, c1), (t2, c2)) => (t1+t2, c1+c2)}.mapValues{case (t,c) => t/c}.collect()
  }

  def convertRawRddToStationsRdd(lines: RDD[String]): RDD[(String, Location)] = {
    lines.map(line => {
      line.split(",") match {
        case a if (a.length == 4) => (s"""${a(0)}-${a(1)}""", Location(safeToDouble(a(2)), safeToDouble(a(3))))
        case a if (a.length == 3) => (s"""${a(0)}-""",        Location(safeToDouble(a(1)), safeToDouble(a(2))))
        case a if (a.length == 2) => (s"""${a(0)}-${a(1)}""", Location(NO_DATA, NO_DATA))
        case a if (a.length == 1) => (s"""${a(0)}-""", Location(NO_DATA, NO_DATA))
        case _ => throw new Exception("line: " + line)
      }
    })
  }

  def convertRawRddtoTemperaturesRdd(lines: RDD[String], year: Int): RDD[(String, (LocalDate, Double))] = {

    lines.map(line => {
      line.split(",")  match {
        case a if (a.length == 5) => (s"""${a(0)}-${a(1)}""", (LocalDate.of(year, a(2).toInt, a(3).toInt), toCelsius(a(4))))
        case a if (a.length == 4) => (s"""${a(0)}-""",        (LocalDate.of(year, a(1).toInt, a(2).toInt), toCelsius(a(3))))
        case _ => throw new Exception("line: " + line)
      }
    })
  }

  private def safeToDouble(v: String): Double = if(!v.isEmpty) v.toDouble else NO_DATA
  private def toCelsius(f: String): Double = (f.toDouble - 32)/ 1.8



}


