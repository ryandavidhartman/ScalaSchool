class NaiveList(val item: Int, next: NaiveList = null) {
  val getNext: NaiveList = next
  def foreach(f: Int => Unit): Unit = {
    f(item)
    if(getNext != null) next.foreach(f) else ()
  }
  def apply(index: Int): Int = {
    if(index == 0)
      item
    else if(getNext != null)
      next.apply(index-1)
    else
      throw new IllegalArgumentException("bad index")
  }
  def add(i: Int): NaiveList = new NaiveList(i, this)
}

val list = new NaiveList(5).add(4).add(3).add(2).add(1).add(0)

list.foreach(i => println(i))
list(3)
