# flatMap

## Overview
_flatMap_ i like using map

## Why would I ever need flatMap?

In practice usage of _flatMap_ naturally occurs in conjuction with _Option_, _Try_ and _Future_
collections.

Here is an example:

```scala
def makeInt(s: String): Try[Int] = Try {
    s.toInt
  }

val a = makeInt("1")
val b = makeInt("2")

val c = a.map { a1 => b.map { b1 => a1 + b1 } }

This evaluates to : c = Success(value = Success(value = 3))
```

So if we just use a map we get a result the is of type Try[Try[Int]] which probably isn't 
what we want.

```scala
val c = a.flatMap { a1 => b.amp { b1 => a1 + b1 } }
```

This evaluates to Success(value = 3) (Type Try[Int])

So we want to combine two vals of type _Try_ how naturally leads to using _flatMap_. Similarly when
we just _Options_, _Either_, and _Future_ we'll find ourselves using _flatMap_.

