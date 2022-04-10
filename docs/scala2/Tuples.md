Pairs and Tuples are defined as below:

A tuple combines a fixed number of items together so that they can be passed around as a whole.  Unlike an array
or list, a tuple can hold objects with different types.

<!-- code -->
```scala
    val pair = ("answer", 42)   // type: (String, Int)
    val (label, value) = pair   // label = "answer", value = 42  
    pair._1 // "answer"  
    pair._2 // 42  
```