# What is currying

Here is one definition:

Converting a function with multiple arguments into a function with a
single argument that returns another function.

Here is alternative definition:

The technique of translating the evalution of a function that takes mulitple arguments
into evaluating a sequence of functions each with a single argument.

Imagine replacing x = f(x1,x2,x3) with g(x1)(x2)(x3).

Where:
```text
h = g(x1) where h also is a function of 1 variable
i  = h(x2) again i is a function of 1 variable

so finally x = i(x3) = h(x2)(x3) = g(x1)(x2)(x3)
```

Example say f(x,y,z) = x+y+z
```text
f(x1,y1,z1) = g(x1)(y1)(z1) 

with h(x) = g(x1) = x + x1
i(x) = h(x2) = g(x1)(x2) = x + x1 + x2
i(x3) = x1 + x2 + x3   
```

<!-- code -->
```scala
    def f(a: Int, b: Int): Int // uncurried version (type is (Int, Int) => Int)
    def f(a: Int)(b: Int): Int // curried version (type is Int => Int => Int)
```

![currying](imgs/rtjvmCurrying.png)

![currying2](imgs/rtjvmCurrying_2.png)
