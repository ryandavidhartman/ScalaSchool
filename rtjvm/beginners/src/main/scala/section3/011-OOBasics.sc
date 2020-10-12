
class Writer(firstName: String, surname: String, val year: Int) {

  def fullName(): String = s"$firstName $surname"

  override def equals(w: Any): Boolean = {
    println("Hey I'm using your equals function!\n")

     super.equals(w)
     /*
     Here is a equality check could use


    if (!w.isInstanceOf[Writer]) {
      false
    } else {
      val writer = w.asInstanceOf[Writer]
      this.hashCode() == writer.hashCode()
    } */

  }

  override def hashCode(): Int = {
    import java.util.Objects
    Objects.hash(firstName, surname, year)
  }
}

class Novel(name: String, year: Int, author: Writer) {
  lazy val authorAge = year - author.year
  def isWrittenBy(author: Writer): Boolean = author == this.author
  def copy(newYear:Int): Novel = new Novel(name, newYear, author)
}

val a1 = new Writer("bob", "smith", 2001)
val a2 = new Writer("bob", "smith", 2001)
println(s"a1 == a2? ${a1 == a2}")

val n1 = new Novel("war and peace", 2020, a1)
n1.isWrittenBy(a2)


class Counter(val count: Int = 0) {
  def inc = {
    val newCount = count+1
    println(s"Incrementing by one, counter=$newCount")
    new Counter(newCount)
  }
  def dec = {
    val newCount = count-1
    println(s"Decrementing by one, counter=$newCount")
    new Counter(newCount)
  }

  def inc(n: Int):Counter = if(n < 0) this else inc.inc(n-1)
  def dec(n: Int):Counter = if(n < 0) this else dec.dec(n-1)

  def print() = "Current count: " + count
}

val counter = new Counter()
println("First run: " + counter.inc.print())
println("Second run: " + counter.inc(10).print())
