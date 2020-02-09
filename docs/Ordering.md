There is already a class in the standard library that represents orderings: `scala.math.Ordering[T]` which contains
comparison functions such as `lt()` and `gt()` for standard types. Types with a single natural ordering should inherit from 
the trait `scala.math.Ordered[T]`.


<!-- code -->
```scala
    import math.Ordering  

    def msort[T](xs: List[T])(implicit ord: Ordering) = { ...}  
    msort(fruits)(Ordering.String)  
    msort(fruits)   // the compiler figures out the right ordering  
```