package stackoverflow

import org.apache.spark.{RangePartitioner, SparkConf, SparkContext}
import org.apache.spark.SparkContext._
import org.apache.spark.rdd.RDD

import annotation.tailrec
import scala.reflect.ClassTag

/** A raw stackoverflow posting, either a question or an answer */
case class Posting(postingType: Int, id: Int, acceptedAnswer: Option[Int], parentId: Option[Int], score: Int, tags: Option[String]) extends Serializable


/** The main class */
object StackOverflow extends StackOverflow {

  @transient lazy val conf: SparkConf = new SparkConf().setMaster("local").setAppName("StackOverflow")
  @transient lazy val sc: SparkContext = new SparkContext(conf)

  /** Main function */
  def main(args: Array[String]): Unit = {

    val t0 = System.currentTimeMillis()
    val lines   = sc.textFile("src/main/resources/stackoverflow/stackoverflow.csv")
    val t1 = System.currentTimeMillis()
    val raw     = rawPostings(lines)
    val t2 = System.currentTimeMillis()
    val grouped = groupedPostings(raw)
    val t3 = System.currentTimeMillis()
    val scored  = scoredPostings(grouped)
    val t4 = System.currentTimeMillis()
    //    checkScores(scored)
    val vectors = vectorPostings(scored)
    val t5 = System.currentTimeMillis()
    //    checkVectors(vectors)

    val means   = kmeans(sampleVectors(vectors), vectors, debug = true)
    val t6 = System.currentTimeMillis()

    val results = clusterResults(means, vectors)
    printResults(results)
    val time1 = (t1-t0)/1000.0
    val time2 = (t2-t1)/1000.0
    val time3 = (t3-t2)/1000.0
    val time4 = (t4-t3)/1000.0
    val time5 = (t5-t4)/1000.0
    val time6 = (t6-t5)/1000.0
    val total = (t6-t0)/1000.0
    Seq[Double](time1, time2, time3, time4, time5, time6, total).map(println(_))
  }

  private def checkScores(scoreRdd:RDD[(Posting, Int)]): Unit = {
    val scorcedCollected = scoreRdd.collect()
    assert(scorcedCollected.contains(Posting(1,6,None,None,140,Some("CSS")),67),  "CSS Answer is wrong")
    assert(scorcedCollected.contains(Posting(1,42,None,None,155,Some("PHP")),89),  "PHP Answer is wrong")
    assert(scorcedCollected.contains(Posting(1,72,None,None,16,Some("Ruby")),3),   "Ruby Answer is wrong")
    assert(scorcedCollected.contains(Posting(1,126,None,None,33,Some("Java")),30), "Java Answer is wrong")
    assert(scorcedCollected.contains(Posting(1,174,None,None,38,Some("C#")),20),   "C# Answer is wrong")
  }

  private def checkVectors(vectorRdd: RDD[(Int, Int)]): Unit = {
    val vectors = vectorRdd.collect()
    assert(vectors.length == 2121822, "Incorrect number of vectors: " + vectors.length)
    assert(vectors.contains((350000,67)))
    assert(vectors.contains((100000,89)))
    assert(vectors.contains((300000,3)))
    assert(vectors.contains((50000,30)))
    assert(vectors.contains((200000,20)))
  }
}


/** The parsing and kmeans methods */
class StackOverflow extends Serializable {

  /** Languages */
  val langs =
    List(
      "JavaScript", "Java", "PHP", "Python", "C#", "C++", "Ruby", "CSS",
      "Objective-C", "Perl", "Scala", "Haskell", "MATLAB", "Clojure", "Groovy")

  /** K-means parameter: How "far apart" languages should be for the kmeans algorithm? */
  def langSpread = 50000

  assert(langSpread > 0, "If langSpread is zero we can't recover the language from the input data!")

  /** K-means parameter: Number of clusters */
  def kmeansKernels = 45

  /** K-means parameter: Convergence criteria */
  def kmeansEta: Double = 20.0D

  /** K-means parameter: Maximum iterations */
  def kmeansMaxIterations = 120


  //
  //
  // Parsing utilities:
  //
  //

  /** Load postings from the given file */
  def rawPostings(lines: RDD[String]): RDD[Posting] =
    lines.map(line => {
      val arr = line.split(",")
      Posting(postingType = arr(0).toInt,
        id = arr(1).toInt,
        acceptedAnswer = if (arr(2) == "") None else Some(arr(2).toInt),
        parentId = if (arr(3) == "") None else Some(arr(3).toInt),
        score = arr(4).toInt,
        tags = if (arr.length >= 6) Some(arr(5).intern()) else None)
    })


  /** Group the questions and answers together */
  def groupedPostings(postings: RDD[Posting]): RDD[(Int, Iterable[(Posting, Posting)])] = {
    val questions = postings.filter(_.postingType == 1).map { p => (p.id, p) }
    val answers = postings.filter(_.postingType == 2).map { p => (p.parentId.get, p) }
    questions.join(answers).groupByKey()
  }


  /** Compute the maximum score for each posting */
  def scoredPostings(grouped: RDD[(Int, Iterable[(Posting, Posting)])]): RDD[(Posting, Int)] = {

    def answerHighScore(as: Array[Posting]): Int = {
      var highScore = 0
      var i = 0
      while (i < as.length) {
        val score = as(i).score
        if (score > highScore)
          highScore = score
        i += 1
      }
      highScore
    }

    grouped.map { case (_, qAndAs) => (qAndAs.head._1, answerHighScore(qAndAs.map { case (_, a) => a }.toArray)) }
  }


  /** Compute the vectors for the kmeans */
  def vectorPostings(scored: RDD[(Posting, Int)]): RDD[(Int, Int)] = {
    /** Return optional index of first language that occurs in `tags`. */
    def firstLangInTag(tag: Option[String], ls: List[String]): Option[Int] = {
      if (tag.isEmpty) None
      else if (ls.isEmpty) None
      else if (tag.get == ls.head) Some(0) // index: 0
      else {
        val tmp = firstLangInTag(tag, ls.tail)
        tmp match {
          case None => None
          case Some(i) => Some(i + 1) // index i in ls.tail => index i+1
        }
      }
    }

    val vctrs = scored.map {case (p, s) => (firstLangInTag(p.tags, langs).get*langSpread,s)}
    val tunedPartitionarer = new RangePartitioner(8, vctrs)
    vctrs.partitionBy(tunedPartitionarer).persist()
  }


  /** Sample the vectors */
  def sampleVectors(vectors: RDD[(Int, Int)]): Array[(Int, Int)] = {

    assert(kmeansKernels % langs.length == 0, "kmeansKernels should be a multiple of the number of languages studied.")
    val perLang = kmeansKernels / langs.length

    // http://en.wikipedia.org/wiki/Reservoir_sampling
    def reservoirSampling(lang: Int, iter: Iterator[Int], size: Int): Array[Int] = {
      val res = new Array[Int](size)
      val rnd = new util.Random(lang)

      for (i <- 0 until size) {
        assert(iter.hasNext, s"iterator must have at least $size elements")
        res(i) = iter.next
      }

      var i = size.toLong
      while (iter.hasNext) {
        val elt = iter.next
        val j = math.abs(rnd.nextLong) % i
        if (j < size)
          res(j.toInt) = elt
        i += 1
      }

      res
    }

    val res =
      if (langSpread < 500)
      // sample the space regardless of the language
        vectors.takeSample(false, kmeansKernels, 42)
      else
      // sample the space uniformly from each language partition
        vectors.groupByKey.flatMap({
          case (lang, vectors) => reservoirSampling(lang, vectors.toIterator, perLang).map((lang, _))
        }).collect()

    //assert(res.length == kmeansKernels, res.length)
    res
  }


  //
  //
  //  Kmeans method:
  //
  //

  /** Main kmeans computation */
  @tailrec final def kmeans(means: Array[(Int, Int)], vectors: RDD[(Int, Int)], iter: Int = 1, debug: Boolean = false): Array[(Int, Int)] = {

    val pointsIntoMeans = vectors.map { case p:(Int, Int) => (findClosest(p,  means), p)}.groupByKey().collect()
    val newMeans = means.clone()

    /*
    for(i <- 0 until means.length) {
      newMeans(i) = {
        pointsIntoMeans.find { case (index, points) => index == i } match {
          case Some((_, points)) => averageVectors(points)
        }
      }
    }*/

    pointsIntoMeans.map{case (index, points) => newMeans(index) = averageVectors(points)}

    //assert(newMeans.length == means.length, "nm: " + newMeans.length + " not equal " + means.length)

    val distance = euclideanDistance(means, newMeans)

    if (debug) {
      println(
        s"""Iteration: $iter

           |  * current distance: $distance

           |  * desired distance: $kmeansEta
           |  * means:""".stripMargin)
      for (idx <- 0 until kmeansKernels)
        println(f"   ${means(idx).toString}%20s ==> ${
          newMeans(idx).toString}%20s  " +
          f"  distance: ${euclideanDistance(means(idx), newMeans(idx))}%8.0f")
    }

    if (converged(distance))
      newMeans
    else if (iter < kmeansMaxIterations)
      kmeans(newMeans, vectors, iter + 1, debug)
    else {
      println(


        "Reached max iterations!")
      newMeans
    }
  }




  //
  //
  //  Kmeans utilities:
  //
  //

  /** Decide whether the kmeans clustering converged */
  def converged(distance: Double) =
    distance < kmeansEta


  /** Return the euclidean distance between two points */
  def euclideanDistance(v1: (Int, Int), v2: (Int, Int)): Double = {
    val part1 = (v1._1 - v2._1).toDouble * (v1._1 - v2._1)
    val part2 = (v1._2 - v2._2).toDouble * (v1._2 - v2._2)
    part1 + part2
  }

  /** Return the euclidean distance between two points */
  def euclideanDistance(a1: Array[(Int, Int)], a2: Array[(Int, Int)]): Double = {
    assert(a1.length == a2.length)
    var sum = 0d
    var idx = 0
    while(idx < a1.length) {
      sum += euclideanDistance(a1(idx), a2(idx))
      idx += 1
    }
    sum
  }

  /** Return the closest point */
  def findClosest(p: (Int, Int), centers: Array[(Int, Int)]): Int = {
    var bestIndex = 0
    var closest = Double.PositiveInfinity
    for (i <- 0 until centers.length) {
      val tempDist = euclideanDistance(p, centers(i))
      if (tempDist < closest) {
        closest = tempDist
        bestIndex = i
      }
    }
    bestIndex
  }


  /** Average the vectors */
  def averageVectors(ps: Iterable[(Int, Int)]): (Int, Int) = {
    val iter = ps.iterator
    var count = 0
    var comp1: Long = 0
    var comp2: Long = 0
    while (iter.hasNext) {
      val item = iter.next
      comp1 += item._1
      comp2 += item._2
      count += 1
    }
    ((comp1 / count).toInt, (comp2 / count).toInt)
  }




  //
  //
  //  Displaying results:
  //
  //
  def clusterResults(means: Array[(Int, Int)], vectors: RDD[(Int, Int)]): Array[(String, Double, Int, Int)] = {
    val closest = vectors.map(p => (findClosest(p, means), p))

    val closestGrouped:RDD[(Int, Iterable[(Int, Int)])] = closest.groupByKey().cache()

    val median =
      closestGrouped.mapValues { vs =>
        val clusterSize: Int = vs.toList.length
        val langIndex = vs.map{ _._1/langSpread}.sum / clusterSize
        //vs.foreach {case (x,_) => println("Ryan: "+ x + " clusterSize" + clusterSize)}
        assert(langIndex < langs.length && langIndex >= 0, "langIndex = " + langIndex)
        val langLabel: String   = langs(langIndex)
        // most common language in the cluster
        val langPercent: Double = (vs.map{ case(x,y) => x}.filter(x => x/langSpread == langIndex).toList.length / clusterSize) * 100.0
        // percent of the questions in the most common language

        //val medianScore: Int    = vs.map{ case(x,y) => y}.sum / clusterSize
        val medianScore: Int = calcMedian(vs.map{ case(x,y) => y}.toSeq)


        (langLabel, langPercent, clusterSize, medianScore)
      }

    median.collect().map(_._2).sortBy(_._4)
  }

  def printResults(results: Array[(String, Double, Int, Int)]): Unit = {
    println("Resulting clusters:")
    println("  Score  Dominant language (%percent)  Questions")
    println("================================================")
    for ((lang, percent, size, score) <- results)
      println(f"${score}%7d  ${lang}%-17s (${percent}%-5.1f%%)      ${size}%7d")
  }

  def calcMedian(s: Seq[Int])  =
  {
    val (lower, upper) = s.sortWith(_<_).splitAt(s.size / 2)
    if (s.size % 2 == 0) (lower.last + upper.head) / 2 else upper.head
  }
}