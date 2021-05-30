Converting a function with multiple arguments into a function with a
single argument that returns another function.

<!-- code -->
```scala
    def f(a: Int, b: Int): Int // uncurried version (type is (Int, Int) => Int)
    def f(a: Int)(b: Int): Int // curried version (type is Int => Int => Int)
```

![currying](imgs/rtjvmCurrying.png)

![currying2](imgs/rtjvmCurrying_2.png)
