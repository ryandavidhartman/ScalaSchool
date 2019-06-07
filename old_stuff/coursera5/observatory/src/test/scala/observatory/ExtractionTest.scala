package observatory

import java.time.LocalDate

import org.junit.runner.RunWith
import org.scalatest.{BeforeAndAfterAll, FunSuite}
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ExtractionTest extends FunSuite with BeforeAndAfterAll {

  import Extraction._
  import SparkSessionWrapper._

  override def afterAll(): Unit = {
    spark.stop()
  }

  test("test convertRawRddToStationsRdd") {
    import spark.implicits._
    import Constants._


    val filePath = fsPath("/stations.csv")
    val rdd = spark.sparkContext.textFile(filePath)
    val stationRdd = convertRawRddToStationsRdd(rdd)

    //stationRdd.toDF().show()
    assert(stationRdd.count === 29444)

    val filteredStationRdd = stationRdd.filter{case (_, l) => l.lat != NO_DATA && l.lon != NO_DATA}
    //filteredStationRdd.toDF().show()
    assert(filteredStationRdd.count == 27708)  // ??? 27722
  }

  test("test convertRawRddtoTemperaturesRdd") {
    import spark.implicits._


    val filePath = fsPath("/1991.csv")
    val rdd = spark.sparkContext.textFile(filePath)
    val tempRdd = convertRawRddtoTemperaturesRdd(rdd, 1991)

    tempRdd.take(20).map {println }

    assert(tempRdd.count === 2563229)

  }

  test("test locationYearlyAverageRecords") {

    val date1 = LocalDate.now()

    val records:List[(LocalDate, Location, Double)] = List(
      (date1, Location(121.12, 34.67), 10.0),
      (date1, Location(121.12, 34.67), 20.0),
      (date1, Location(121.12, 34.67), 30.0),
      (date1, Location(101.12, 36.67), 40.0),
      (date1, Location(101.12, 36.67), 50.0),
      (date1, Location(101.12, 36.67), 60.0))

    val results = locationYearlyAverageRecords(records).toList

    assert(results.length === 2)
    assert(results.contains((Location(121.12, 34.67), 20.0)))
    assert(results.contains((Location(101.12, 36.67), 50.0)))
  }

}