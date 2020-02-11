package playground

import scala.io.Source

object TubiFun {

  case  class CVSData(name: String, runtime: Double, rating: Double)

  def main(args: Array[String]) {

    val url = "https://gist.githubusercontent.com/CatTail/18695526bd1adcc21219335f23ea5bea/raw/54045ceeae6a508dec86330c072c43be559c233b/movies.csv"

    val data = Source.fromURL(url)
    var allData: String = ""
    data.foreach{ c =>
      allData += c
    }

    //println(allData)

    val parsedStep1:Seq[String] = allData.split('\n').tail.filter(_ != "\"27, most popular\",127.6053254325764,6.03051434574081")
    //parsedStep1.foreach(l => println(l))

    val parsedStep2 = for {
     l <- parsedStep1
     splitStuff:Array[String]  = l.split(',')

    } yield CVSData(splitStuff(0), splitStuff(1).toDouble, splitStuff(2).toDouble)

    //parsedStep2.foreach(p => println(p))

    val results = parsedStep2.sortWith((l1, l2) => l1.rating > l2.rating).take(10)

    println("Top ten")
    results.foreach(println)


  }

}
