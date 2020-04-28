# Scala Stream Class  // LazyList in Scala 2.13

In Scala 2.13 the `Stream` class was deprecated in favor of the `LazyList` class.  As its name
suggests, a LazyList is a linked list whose elements are lazily evaluated. An important semantic
difference with Stream is that in LazyList both the head and the tail are lazy, whereas in
Stream only the tail is lazy.

The LazyList type is a _lazy_ collection, generated from one or more starting elements and a
recursive function.  Elements are added to the collection only when they are accessed for the
first time, in contrast to other immutable collections that receive 100% of their contents
at instantiation time.  The elements that a stream generates are cached for later retrieval,
ensuring that each element is generated once.  Streams can be unbounded, theoretically 
infinite collections where elements are only realized upon access.  They can be terminated 
with `LazyList.empty`, a counterpart to `List.empty`.

Steams,like lists, are recursive data structures consisting of a head (the current element)
and a tail (the rest of the collection).  They can be built with a function that returns a 
new stream containing the head element and a recursive invocation of that function to build
the tail.  You can use `LazyList.cons` to construct a new stream with the head and tail.

Here is an example function that builds and recursively generates a new stream.  By
incrementing the starting value, it will end up creating a collection of consecutively 
increasing integers:

```scala
scala> def inc(i: Int): LazyList[Int] = LazyList.cons(i, inc(i+1))
def inc(i: Int): LazyList[Int]

scala> val s = inc(1)
val s: LazyList[Int] = LazyList(<not computed>)
```

We have our lazy list but it contains no values.  This is one difference from the 2.12 
`Stream` where the head value _would_ have been computed.  Lets force it to build out the 
next few elements by "taking" them retrieving the contents as regular list:

```scala
scala> val l = s.take(5).toList
val l: List[Int] = List(1, 2, 3, 4, 5)

scala> s
val res0: LazyList[Int] = LazyList(1, 2, 3, 4, 5, <not computed>)
```
We took the first five elements and retrieved them as a plain old list.  Printing out the
original stream (lazy list) instances shows that it now contains five elements and is ready
to generate more.  We could follow this up by taking 20, or 200, or 2000 elements.  The stream
contains a recursive function call (specifically a function value) that is can use to generate 
new elements without end.

An alternative syntax for the `LazyList.cons` operator is the slightly cryptic `#::` operator,
which we'll just call the _cons_ operator for streams.  This performs the same function as
`LazyList.cons` except with right-associative notation, complementing the cons operator for
lists `::`

Here's the "inc" function again using the cons operator `#::`.  I've also renamed the parameter
to "head" to better demonstrate its use the head element of the new LazyList instance:

```scala
scala> def inc(head: Int): LazyList[Int] = head #:: inc(head+1)
def inc(head: Int): LazyList[Int]

scala> inc(10).take(10)
val res22: scala.collection.immutable.LazyList[Int] = LazyList(<not computed>)

scala> inc(10).take(10).toList
val res23: List[Int] = List(10, 11, 12, 13, 14, 15, 16, 17, 18, 19)
```

Finally, let's try creating a bounded lazy list.  We'll use two arguments to our recursive function,
one specifying the new head element and another specifying the last element to add:

```scala

scala> def to(head: Char, end: Char): LazyList[Char] =
             if(head > end) LazyList.empty
             else head #:: to((head+1).toChar, end)

def to(head: Char, end: Char): LazyList[Char]

scala> val someChars = to('A', 'F').take(10000).toList
val someChars: List[Char] = List(A, B, C, D, E, F)

```

Using the new "to" function, we were able to create a bounded LazyList consisting of the characters
A to F.  Note the LazyList's `take` operation only returned the available elements, ending after we
placed `LazyList.empty` to terminate the collection.
 





