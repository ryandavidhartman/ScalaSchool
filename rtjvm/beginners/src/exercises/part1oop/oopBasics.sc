import sun.java2d.SurfaceDataProxy.CountdownTracker

class Writer(firstName: String, surname: String, val year: Int) {

  def fullname(): String = s"$firstName $surname"

  override def equals(w: Any): Boolean = {
    println("Hey I'm using your equals function!\n")
    // probably what it is doing w.hashCode() == this.hashCode()
    super.equals(w)
  }


}

class Novel(name: String, year: Int, author: Writer) {

  val authorAge = year - author.year
  def isWrittenBy(author: Writer): Boolean = author == this.author
  def copy(newYear:Int): Novel = new Novel(name, newYear, author)


}

val a1 = new Writer("bob", "smith", 2001)
val a2 = new Writer("steve", "roberts", 1990)
println(s" is a1 = a2? ${a1 == a2}")
val n1 = new Novel("war and peace", 2020, a1)
n1.isWrittenBy(a2)


class Counter(val count: Int = 0) {
  def increment = {
    val newCount = count+1
    println(s"Incrementing by one, counter=$newCount")
    new Counter(newCount)
  }
  def decrement = {
    val newCount = count-1
    println(s"Decrementing by one, counter=$newCount")
    new Counter(newCount)
  }

  def increment(n: Int):Counter = if(n <0) this else increment.increment(n-1)
  def decrement(n: Int):Counter = if(n <0) this else decrement.decrement(n-1)
}

val counter = new Counter()
counter.increment
