## Partial Functions

Functions like `def double(x: Int) = x*2`, are known as _total functions_, because they
properly support every possible value that meets the type of the input parameters.  That
is to say `double` will successfully return a value for every integer value.

However, there are some functions that do not support every possible value that meets the
input type requirement.  For example, a function that returns the square root of the input
number will not work if a negative value was passed in as a function argument.  Likewise,
a function that divides by a given number isn't applicable if that number is zero.  Such
functions are called _partial functions_ because they can only partially apply to their 
input domain.

Scala's partial functions are function literals that apply a series of case patterns to
their input, requiring that the input match at least one of the given patterns.  Invoking
one of these partial functions with data that does not meet at least one case pattern results
in a Match exception.

### Note On Partial Functions vs Partially Applied Functions
The two terms look and sound almost the same, causing many developers to mix them up.  A
partial function, as opposed to a total function only accepts a partial amount of all the 
possible input values.  A partially applied function is a regular function that has been
partially invoked, (i.e. invoked with less than the total number of parameters) and remains
a function that can be fully invoked in later.

### Creating Partial Functions

A set of case clauses enclosed by braces is the syntax for writing a partial function.

```scala
val f: PartialFunction[Char, Int] = {
  case '+' => 1
  case '-' => -1
}

f('-')  // calls f.apply('-') and returns -1
f.isDefinedAt('0')  // returns false
f('0') // Throws a Match Error
```

`f` is an instance of the class `PartialFunction[A,B]` (Where `A` is the input type and `B` 
return type)

The `PartialFunction` class has two methods: `apply`, which computes the function value from
the matching pattern, and `isDefinedAt` which return `true` if the input matches at least one
of the patterns.

`PartialFunction[A,B]` is a subtype of `Function1[A,B]` so partial functions can be used where
ever regular functions are accepted.

There are some methods specially designed to be used with `PartialFunctions`as a parameter.  For
example, the `collect` method of the `GenTraversable` trait applies a partial function to all
the elements where it is defined, and returns a sequence of the results.

```scala
"-3+4".collect {
  case '+' => 1
  case '-' => -1 
}

// returns Vector(-1, 1)
```

### Note on Seq and Map
A `Seq[A]` is also a `PartialFunction[Int, A]`  and a `Map[K,V]` is a `PartialFunction[K,V]`.  So
for example you can pass `Maps` and `Seqs` into the `collect` method.  (Since collect takes a PF)

```scala
val names = Array("Alice", "Bob", "Sally")
val scores = Map("Alice" -> 10, "Carmen" -> 7)

names.collect(scores)  // Yields Array(10, 7)

List(2,1,0).collect(names)  // Yields List("Sally", "Bob", "Alice")
```