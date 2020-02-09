A for-comprehension is syntactic sugar for `map`, `flatMap` and `filter` operations on collections.

The general form is `for (s) yield e`

- `s` is a sequence of generators and filters
- `p <- e` is a generator
- `if f` is a filter
- If there are several generators (equivalent of a nested loop), the last generator varies faster than the first
- You can use `{ s }` instead of `( s )` if you want to use multiple lines without requiring semicolons
- `e` is an element of the resulting collection

### Example 1

<!-- code -->
```scala
    // list all combinations of numbers x and y where x is drawn from
    // 1 to M and y is drawn from 1 to N
    for (x <- 1 to M; y <- 1 to N)
      yield (x,y)
```

is equivalent to

<!-- code -->
```scala        
    (1 to M) flatMap (x => (1 to N) map (y => (x, y)))
```

### Translation Rules

A for-expression looks like a traditional for loop but works differently internally

`for (x <- e1) yield e2` is translated to `e1.map(x => e2)`

`for (x <- e1 if f) yield e2` is translated to `for (x <- e1.filter(x => f)) yield e2`

`for (x <- e1; y <- e2) yield e3` is translated to `e1.flatMap(x => for (y <- e2) yield e3)`

This means you can use a for-comprehension for your own type, as long
as you define `map`, `flatMap` and `filter`.

For more, see [lecture 6.5](https://class.coursera.org/progfun-2012-001/lecture/111).

### Example 2

<!-- code -->
```scala
    for {  
      i <- 1 until n  
      j <- 1 until i  
      if isPrime(i + j)  
    } yield (i, j)  
```

is equivalent to

<!-- code -->
```scala
    for (i <- 1 until n; j <- 1 until i if isPrime(i + j))
        yield (i, j)  
```

is equivalent to

<!-- code -->
```scala
    (1 until n).flatMap(i => (1 until i).filter(j => isPrime(i + j)).map(j => (i, j)))
```