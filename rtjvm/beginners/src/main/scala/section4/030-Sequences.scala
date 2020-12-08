package section4

import scala.util.Random

object Sequence extends App {
  // Sequences
  val aSequence = Seq(1,2,3,4)  // The default implementation of a Seq  is a List
  println(aSequence)
  println(aSequence.reverse)
  println(aSequence(2))
  println(aSequence ++ Seq(5,6,7))
  println(Seq(3,4, -5, 55, 7).sorted)

  // Ranges
  val aRange: Seq[Int] = 1 to 10
  println(aRange)
  aRange.foreach(println)
  (1 until 10).foreach(println)

  // Lists
  val aList = List(1,2,3)
  val prepended = 42 +: aList  // 42 :: aList works too
  println(prepended)

  val appended = aList :+  42
  println(appended)

  val apples5 = List.fill(5)("apple")
  println(apples5)

  println(aList.mkString("-|-"))

  // Arrays
  val numbers = Array(1,2,3,4)
  val threeElements = Array.ofDim[Int](3)
  println(threeElements) //weird
  println(threeElements.mkString(","))  //ha!  we have a default value of zero!

  // Mutation
  numbers(2) = 0 // syntactic sugar for numbers.update(2,5)!

  // Arrays and Seqs
  val numberSeq: Seq[Int] = numbers  //this works!  Arrays can be implicitly converted to a
                                     // class scala.collection.immutable.ArraySeq$ofInt
  println(numberSeq.mkString(","))
  println(numberSeq.getClass)

  // Vector
  val vector: Vector[Int] = Vector(1,2,3)
  println(vector)

  val maxCapacity = 1000000

  // Vector vs List Performance Test
  def getWriteTime(collection: Seq[Int]): Double = {
    val maxRuns = 10000
    val r = new Random()
    val startTime = System.nanoTime()
    (1 to maxRuns).foreach(collection.updated(r.nextInt(maxCapacity), r.nextInt()))
    (System.nanoTime() - startTime)/maxRuns
  }

  val numbersList = (1 to maxCapacity).toList
  val numberVector = (1 to maxCapacity).toVector

  val listTime = getWriteTime(numbersList)
  val vectorTime = getWriteTime(numberVector)

  println(s"listTime: $listTime vectorTime: $vectorTime  listTime/vectorTime: ${listTime/vectorTime}")
}
