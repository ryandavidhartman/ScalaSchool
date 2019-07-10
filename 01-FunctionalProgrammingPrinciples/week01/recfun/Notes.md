# Welcome to Scala School

## Eventually we'll cover:

* immutable data structures 
* first-class and higher-order functions
* map (and flatMap and filter) reduce/fold etc
* currying
* function composition
* monads
* lazy evaluation

## We won't cover:
* monoids, functors applicatives
* and most of the mathematics
* category theory
* Monads are Monoids in the category of endofuctors.  Wee!

## What is the goal of Scala?
While designing Scala, Prof. Odersky sought to marry the object-oriented programming (OOP) and functional progamming (FP) paradigms. Informally, a programming paradigm refers to a certain style or technique for solving problems using programming.

### Object-oriented programming has three principles with their own features:

* Encapsulation: This principle is primarily concerned with access restriction. Access restriction prohibits one object from accessing the state or values of another object. Encapsulation can also allow for a name to carry different definitions/behaviors when used in separate contexts.
* Inheritance: Broadly, this principle allows for one object to acquire (or inherit) the properties of another object.
* Polymorphism: This principle refers to the same message resulting in different responses from different objects. When given the same message, two objects might retain an abstract similarity but with different details. 

### So what is functional programming?

As for functional programming, we have the following aspects according to Prof. Odersky:

* The absence of mutable variables and assignments. Here, mutation refers to changing some feature of an object while its identity remains the same. So if I say x = 5 and then reassign the value of x by saying x = ‘kombucha', I’ve changed the value of x while preserving its identity.
* A strong focus on implementing business logic using function composition
* Functions are first-class citizens. That is, functions:
    1. may be defined anywhere, even inside other functions
    2. may be passed as parameters and may be returned as results
    3. there exist a set of operators to create functions.

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

## Immutable Data Structures

Using a language that favors expressions over statements, allows us to write pure functions (i.e. without side effects) and this naturally leads us to favor immutable data structors.

By using values over variables, along with pure functions, enables "referential transparency" which we'll see as the course progresses how this will let us write code that is easier to read and debug.
 






