# What is Scala?

[Scala](https://www.scala-lang.org/) combines object-oriented and functional programming in one concise, high-level
language. Scala's static types help avoid bugs in complex applications, and its JVM and JavaScript runtimes let you
build high-performance systems with easy access to huge ecosystems of libraries.

## What is functional programming?
In functional programming, functions are what we call _first-class citizens_. This means that functions are passed as
objects just like integers and arrays. Functions may have functions as parameters and return values, like an other data
type.
We generally contrast functional programming with object oriented programming or imperative programming. In imperative
programming, we use for loops to access all the elements of an array. It’s typical in imperative programming to modify
the elements of an array. In functional programming, we will use functions like _map_ to access every element of a collection,
and rather than modifying the collection, we create a new one. The functional style, while it can take a little to get used to,
can lead to cleaner more concise code.

* functions has first class data types
* Using immutable data and _maps_ instead of looing and data mutation.

Compare the two styles to declar an array and double each element of the array

```java
// Imperative Style
val arr = new Array[Int](100)

{
  var i = 0
  while (i < 100) {
    arr(i) = i
    i = i + 1
  }
}

// mutate the original array

{
  var i = 0
  while (i < 100) {
    arr(i) = arr(i) * 2
    i = i + 1
  }
}
```

```scala
// Functional style
val arr = (0 until 100)

// Use map and make a new Array
val newArr = arr.map(_ * 2)

```

## Pros and Cons of Functional Programming

Functional Programming Benefits:
* Functional code is more concise and easy to reason about.
* Functional abstractions make writing complex code easier. Especially concurrent, asynchronous, distributed, and multi service code.
* Functional programming is closely related to many powerful mathematical abstractions, such as Monads, Monoids, etc…

Functional Programming Downsides:

* Functional code might have performance penalties, especially in the case of lower level code.  (This is one of the reasons Scala supports imperative as well.)
* It takes time to adjust to the functional programming paradigm.
* [Here](https://github.com/ryandavidhartman/ScalaSchool/tree/master/demos/FPCoinFlipGame) is a small app that shows how you can maintain state without using
  mutable variables and how to "loop" in a functional style. 

Still not convinced? [Read the Benefits of Functional Programming](https://alvinalexander.com/scala/fp-book/benefits-of-functional-programming).

## Scala
While Scala is a functional programming language, it supports imperative programming as well. While a pure functional language does not have loops,
loops are part of the Scala language, though are generally used infrequently.

*Scala is a strongly-typed, object-oriented, functional programming language.*

Scala Features:
* Fully functional
* Functions are objects
* Hierarchical type system
* Interoperable with Java code

Scala Benefits:
* Functional code is easy to reason about
* Service composition maps well to functional concepts
* The JVM is high-performance and well understood
* Reuse existing Java code
* Easier to write parallelizable code that scales
* Easier to model asynchronous operations
* Do more with less code

Scala Downsides:
* Slow to compile
* Functional thinking new to some
* Learn a new programming language
* Not as widely used as Java and Python
* Subject to the limitations of the JVM – for example, type erasure for generics

## What most people use Scala For

Popular enterprise frameworks like [Spark](https://spark.apache.org/docs/0.9.1/scala-programming-guide.html), 
[Akka](https://akka.io/) and [Kafka](https://kafka.apache.org/) were written in Scala.  Many scala developers work
on building apps on top of these frameworks.

Scala is used heavily at Twitter, Netflix, and [many others](https://sysgears.com/articles/how-tech-giants-use-scala/)
