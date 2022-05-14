# flatMap

## Overview
_flatMap_ i like using map

## Why would I ever need flatMap

Why would I ever need to transform _one element_ into a _list_ of different elements?

```scala
"foo".map(c => Seq(c)) =  ArraySeq(List('f'), List('o'), List('o'))  //
```

This isn't super helpful but this is pretty cool.

```scala
"foo".flatMap(s => Seq(s)) = Vector('f', 'o', 'o')
```