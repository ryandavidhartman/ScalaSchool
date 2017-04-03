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


    val filePath = fsPath("/stations.csv")
    val rdd = spark.sparkContext.textFile(filePath)
    val stationRdd = convertRawRddToStationsRdd(rdd)

    //stationRdd.toDF().show()

    assert(stationRdd.count === 29444)
  }

  
}