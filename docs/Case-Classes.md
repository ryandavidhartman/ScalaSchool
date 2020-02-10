# Case Classes

```scala
case class Person(name: String, age: Int)
```

Classes defined with the `case` modifier are called _case classes._  Using the modifier cases
the Scala compiler add some syntactic sugar to the class.

* It creates a `companion object` with the a factory method to create a case class without out
requiring the `new` keyword.

```scala
case class Person(name: String, age: Int)
val p = Person("ryan", 21)  //instead of val p = new Person("ryan", 21)
```

* In case classes all of the class parameters are promoted to class fields

```scala
case class Person(name: String, age: Int)
val p = Person("ryan", 21)
println(p.name) //is allowed
```

* The compiler adds "natural" implementations of methods `toString`, `hashCode` and `equals`  these
methods will print, hash and compare a whole tree consisting of the class and (recursively) all its
arguments.

Since `==` in Scala always delegates to equals, this means that elements co case classes are always
compared structurally:

```scala
case class Person(name: String, age: Int)
val p1 = Person("bob", 19)
val p2 = Person("bob", 19)
val p3 = p1.copy()

// Here p1 == p2 == p3 is true
``` 

## Companion Class
A class that shares the same name with a singleton object defined in the same source file.  The
class is the singleton object's companion class

## Companion Object
A singleton object that shares the same name with a class defined in the same source file. Companion
objects and classes have access to each other's private members.  In addition, any implicit
conversions defined in the companion object will be in scope anywhere the class is used.

## Case Classes & Companion Objects
Case classes have automatically generated companion objects

![Class Classes](./imgs/rtjvmCaseClasses.png)
