package observatory

import org.apache.spark.sql._
import org.apache.spark.sql.types._

object SparkSessionWrapper {

  import org.apache.spark.sql.SparkSession
  import org.apache.spark.sql.functions._


  val spark: SparkSession =
    SparkSession
      .builder()
      .appName("Observatory")
      .config("spark.master", "local")
      .getOrCreate()

}
