# Functions

## Cheat Sheet

```scala
def triple(x: Int) = 3 * x  // How to define a function

val f = (x: Int) => 3 * x   // Lambda or anonymous function style

(1 to 10).map(3 * _)        // Function with an anonymous parameter

def greet(x: Int): Unit = { // This has a Unit return type
  println(s"Hello, $x")
}

def greet(x: Int, salutation: String = "Hello"): Unit = {  // Default value
  println(s"$salutation, $x")
}

def sum(xs: Int*): Int = {  // Var Args
  var r = 0; for( x <- xs) r += x  // Semicolon separates statements on the same line
  r // No return statement, Last expression is the value of the block
}

def sum(xs: Int*): Int = // Return type is required for recursive functions
  if (xs.lenght == 0) 0 else xs.head + sum(xs.tail : _*)  // Sequence as varargs
```

## Also see
* [Anonymous Functions](./Anonymous-Functions.md)
* [Higher Order Functions](./Higher-order-functions.md)
* [Partial Functions](./Partial-Functions.md)
* [Recursive Functions](./Recursive-Functions.md)