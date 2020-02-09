# Variance in parametrized types

Given `A <: B`  (i.e. A is a subtype of B)

* If `C[A] <: C[B]`, `C` is covariant
* If `C[A] >: C[B]`, `C` is contravariant
* Otherwise C is invariant

<!-- code -->
```scala
   class C[+A] { ... } // C is covariant
   class C[-A] { ... } // C is contravariant
   class C[A]  { ... } // C is nonvariant
```

Examples:
1. Should `List[T]` be covariant or contravariant?
Well given `Cat <: Animal` Now can I use a `List[Cat]` anywhere a `List[Animal]` is used?  Yes.
This means `List[Cat] <: List[Animal]`.  So `List[T]` follows with subtype arrangement of `T`.  Therefore List is covariant.

2. Ok should `Array[T]` also be covarient?
Turns out no!  Why?  Well arrays are mutable, so if Array[T] *was* covariant you could do:

<!-- code -->
```scala
   val bob = Array[Cat](cat1, cat2, cat3)
   val sally: Array[Any] = bob
   sally(0) = "string"
```
This would be possible since `Cat <: Any` and if `Array[T]` was covariant then we could use a `Array[Cat]` in a place where an `Array[Any]` was required.

3. So in general *mutable* containers are `invariant` and *immutable* containers are `covariant`



# Variance for function parameters and return types
Let's consider variance of function types with respect to their parameters and return types.

If `P1 <: P2` and `R1 <: R2`, then should `P1 => R1 <: P2 => R2` be true?
Nope!  Here is why, say:
* `f1 = Cat => Car`
* `f2 = Animal => Vehicle`

So is `Cat => Car :< Animal => Vehicle`?  Well if it *was* then I could use `f1` anywhere I use `f2`.  But I can't replace f2(animal) with f1(animal).  Therefore  `Cat => Car <: Animal => Vehicle` is out!


Ok what about If `P1 <: P2` and `R1 <: R2`, then should `P2 => R1 <: P1 => R2` be true?
Yes!  
* `f1 = Animal => Car`
* `f2 = Cat => Vehicle`

Can I use f1, anywhere f2 was used?  Yes!  f2 takes an `Cat` and returns a `Vehicle`.  Does this work for f1?  Yep
a call f2(cat) can be replaced with f1(cat).  f1(cat) returns a car.  That matches the expected type for f1!     

Therefore `Functions` must be contravariant in their *argument* types and covariant in their *result* types, e.g.

# Summary

<!-- code -->
```scala
  class Seq[+T] Lists etc. are covariant, and can be because they are immutable

  class Array[+T] {
    def update(x: T)
  } // variance checks fails

 trait Function1[-T, +U] {
    def apply(x: T): U
  } // Variance check is OK because T is contravariant and U is covariant
```

Find out more about variance in
[lecture 4.4](https://class.coursera.org/progfun-2012-001/lecture/81)
and [lecture 4.5](https://class.coursera.org/progfun-2012-001/lecture/83)