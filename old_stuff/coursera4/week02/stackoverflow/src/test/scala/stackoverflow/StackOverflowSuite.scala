package stackoverflow

import org.scalatest.{FunSuite, BeforeAndAfterAll}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.rdd.RDD
import java.net.URL
import java.nio.channels.Channels
import java.io.File
import java.io.FileOutputStream

@RunWith(classOf[JUnitRunner])
class StackOverflowSuite extends FunSuite with BeforeAndAfterAll {

  import StackOverflow._

  val posting1 = Posting(postingType = 1, id=1, acceptedAnswer = Some(2), parentId = None,    score= 10, tags= Some("Scala"))
  val posting2 = Posting(postingType = 2, id=2, acceptedAnswer = None,    parentId = Some(1), score= 17, tags= Some("Scala"))
  val posting3 = Posting(postingType = 2, id=3, acceptedAnswer = None,    parentId = Some(1), score= 14, tags= Some("Scala"))
  val posting4 = Posting(postingType = 2, id=4, acceptedAnswer = None,    parentId = Some(1), score= 11, tags= Some("Scala"))
  val posting5 = Posting(postingType = 1, id=5, acceptedAnswer = Some(8), parentId = None,    score= 10, tags= Some("Java"))
  val posting6 = Posting(postingType = 2, id=6, acceptedAnswer = None,    parentId = Some(5), score= 18, tags= Some("Java"))
  val posting7 = Posting(postingType = 2, id=7, acceptedAnswer = None,    parentId = Some(5), score= 14, tags= Some("Java"))
  val posting8 = Posting(postingType = 2, id=8, acceptedAnswer = None,    parentId = Some(5), score= 11, tags= Some("Java"))

  val testPostings = List(posting1, posting2, posting3, posting4, posting5, posting6, posting7, posting8)


  lazy val testObject = new StackOverflow {
    override val langs =
      List(
        "JavaScript", "Java", "PHP", "Python", "C#", "C++", "Ruby", "CSS",
        "Objective-C", "Perl", "Scala", "Haskell", "MATLAB", "Clojure", "Groovy")
    override def langSpread = 50000
    override def kmeansKernels = 45
    override def kmeansEta: Double = 20.0D
    override def kmeansMaxIterations = 120
  }

  override def afterAll(): Unit = {
    sc.stop()
  }

  test("testObject can be instantiated") {
    val instantiatable = try {
      testObject
      true
    } catch {
      case _: Throwable => false
    }
    assert(instantiatable, "Can't instantiate a StackOverflow object")
  }

  test("test grouped") {
    val rdd =sc.parallelize(testPostings)
    val grouped = groupedPostings(rdd)

    val results = grouped.collect()
    //results.foreach(r => println(r))
    assert(results.length == 2)
  }

  test("test find max score") {
    val rdd =sc.parallelize(testPostings)
    val grouped = groupedPostings(rdd)
    val maxScores = scoredPostings(grouped).collect()

    assert(maxScores.length == 2)
    assert(maxScores.head._2 == 17)
  }

  test("test find vectors") {
    val rdd =sc.parallelize(testPostings)
    val grouped = groupedPostings(rdd)
    val maxScores = scoredPostings(grouped)
    val vectors = vectorPostings(maxScores).collect()

    assert(vectors.contains((langSpread*langs.indexOf("Java"),18)))
    assert(vectors.contains((langSpread*langs.indexOf("Scala"),17)))
  }


}
