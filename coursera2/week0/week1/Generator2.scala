trait Generator2[+T] {
  self => // an alias for "this"
  def generate: T

  def map[S](f: T => S): Generator2[S] = new Generator2[S] {
    def generate = f(self.generate)
  }

  def flatMap[S](f: T => Generator2[S]): Generator2[S] = new Generator2[S] {
    def generate = f(self.generate).generate
  }
}

val integers = new Generator2[Int] {
  val rand = new java.util.Random
  def generate = rand.nextInt()
}

val booleans = for (x <- integers) yield x > 0

def pairs[T,U](t: Generator2[T], u: Generator2[U]) = for {
  x <- t
  y <- u
} yield (x, y)

def single[T](x: T): Generator2[T] = new Generator2[T] {
  def generate = x
}

def choose(low: Int, high: Int): Generator2[Int] =
  for (x <- integers) yield low + x % (hi - lo)

def oneOf[T](xs: T*): Generator2[T] =
  for (index <- choose(0, xs.lenght)) yield xs(index)

def lists: Generator2[List[Int]] = for {
  isEmpty <- booleans
  list <- if (isEmpty) emptyLists else nonEmptyLists
} yield list

def emptyLists = single(Nil)

def nonEmptyLists = for {
  head <- integers
  tail <- lists
} yield head :: tail

trait Tree

case class Inner(left: Tree, right: Tree) extends Tree

case class Leaf(x: Int) extends Tree

def leaves: Generator2[Leaf] = for {
  x <- integers
} yield Leaf(x)

def inners: Generator2[Inner] = for {
  l <- trees
  r <- trees
} yield Inner(l,r)

def trees: Generator2[Tree] = for {
  isLeaf <- booleans
  tree <- if(isLeaf) leaves else inners
} yield tree
