#Welcome to Scala School

## Eventually we'll cover:

* first-class and higher-order functions
* map (and flatMap and filter) reduce/fold etc
* currying
* function composition
* monads
* lazy evaluation
* immutable data structures

## We won't cover:
* monoids, functors applicatives
* and most of the mathematics
* category theory
* Monads are Monoids in the category of endofuctors.  Wee!


## So what is functional programming?

### From the wikipedia
In functional code, the output value of a function depends only on the arguments that are passed to the function, so calling a function f twice with the same value for an argument x produces the same result f(x) each time.

Eliminating side effects, i.e., changes in state that do not depend on the function inputs, can make it much easier to understand and predict the behavior of a program, which is one of the key motivations for the development of functional programming.

"pure functions" -> Referential Transparency enabled and the evolution mode talked about in the lectures 


### Compare this to imperative programming

Side effects are a big part of imperative programming.

Imperative means giving orders: do this, do that. The purpose of these orders is to cause side effects. A side effect means that some state somewhere was changed.

Elements in most imperative programming languages can be broken into three classes:

* Control structures: if-then-else, loops, etc.
* Statements: assign a variable, reassign a variable, call a procedure, etc.
* Expressions: code that yields a value.  x+1

In programming language terminology, an “expression” is a combination of values and functions that are combined and interpreted by the compiler to create a new value, as opposed to a “statement” which is just a standalone unit of execution and doesn’t return anything. One way to think of this is that the purpose of an expression is to create a value (with some possible side-effects), while the sole purpose of a statement is to have side-effects.

### In Scala most "control structures" are expressions

```java

int a = 4;
int result;

if (a > 0) {
    result = 1;
} else if(a == 0) {
    result = 0;
} else {
    results = -1;
}

```

```scala
val a = 4

val result = if (a > 0) {
  1
} else if(a == 0) {
  0
} else {
  -1
}

```







