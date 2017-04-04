package observatory

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
    assert(filteredStationRdd.count == 27722)
  }

  test("test convertRawRddtoTemperaturesRdd") {
    import spark.implicits._


    val filePath = fsPath("/1991.csv")
    val rdd = spark.sparkContext.textFile(filePath)
    val tempRdd = convertRawRddtoTemperaturesRdd(rdd, 1991)

    tempRdd.take(20).map {println }

    assert(tempRdd.count === 2563229)

  }



}