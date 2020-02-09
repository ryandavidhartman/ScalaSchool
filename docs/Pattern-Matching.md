Pattern matching is used for decomposing data structures:

<!-- code -->
```scala
    unknownObject match {
      case MyClass(n) => ...
      case MyClass2(a, b) => ...
    }
```

Here are a few example patterns

<!-- code -->
```scala
    (someList: List[T]) match {
      case Nil => ...          // empty list
      case x :: Nil => ...     // list with only one element
      case List(x) => ...      // same as above
      case x :: xs => ...      // a list with at least one element. x is bound to the head,
                               // xs to the tail. xs could be Nil or some other list.
      case 1 :: 2 :: cs => ... // lists that starts with 1 and then 2
      case (x, y) :: ps => ... // a list where the head element is a pair
      case _ => ...            // default case if none of the above matches
    }
```

The last example shows that every pattern consists of sub-patterns: it
only matches lists with at least one element, where that element is a
pair. `x` and `y` are again patterns that could match only specific
types.

### Options

Pattern matching can also be used for `Option` values. Some
functions (like `Map.get`) return a value of type `Option[T]` which
is either a value of type `Some[T]` or the value `None`:
<!-- code -->
```scala
    val myMap = Map("a" -> 42, "b" -> 43)
    def getMapValue(s: String): String = {
      myMap get s match {
        case Some(nb) => "Value found: " + nb
        case None => "No value found"
      }
    }
    getMapValue("a")  // "Value found: 42"
    getMapValue("c")  // "No value found"
```

Most of the times when you write a pattern match on an option value,
the same expression can be written more concisely using combinator
methods of the `Option` class. For example, the function `getMapValue`
can be written as follows: 

<!-- code -->
```scala
    def getMapValue(s: String): String =
      myMap.get(s).map("Value found: " + _).getOrElse("No value found")
```

### Pattern Matching in Anonymous Functions

Pattern matches are also used quite often in anonymous functions:

<!-- code -->
```scala
    val pairs: List[(Char, Int)] = ('a', 2) :: ('b', 3) :: Nil
    val chars: List[Char] = pairs.map(p => p match {
      case (ch, num) => ch
    })
```

Instead of `p => p match { case ... }`, you can simply write `{case ...}`, so the above example becomes more concise:

<!-- code -->
```scala
    val chars: List[Char] = pairs map {
      case (ch, num) => ch
    }
```