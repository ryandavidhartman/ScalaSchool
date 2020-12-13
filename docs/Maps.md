# Map colletions

Maps are Iterables of pairs of keys and values (also named mappings or associations). Scala's Predef class offers an
implicit conversion that lets you write `key -> value` as an  alternate syntax for the pair `(key, value)`. Therefore,
`Map("x" -> 24, "y" -> 25, "z" -> 26)` means exactly the same as `Map(("x", 24), ("y", 25), ("z", 26))`, but reads
better.

The fundamental operations on `Maps` are similiar to those on `Sets`.`

## Map operations fall into the following categories:

### Lookups
`apply`, `get`, `getOrElse`, `contains`, and `isDefinedAt`. These operations turn `Maps` into partial functions from
keys to values. The fundamental lookup method for a `Map` is:

```scala
def get(key): Option[Value]
```

The operation `m get key` tests whether the `Map` contains an association for the given key. If so, it returns the
associated value in a `Some`. If no key is defined in the `Map`, get returns `None`.   `Maps` also define an apply
method that returns the value associated with a given key directly, without wrapping it in an `Option`. If the key is
not defined in the map, an exception is raised.

### Additions and updates
`+`, `++`, and `updated`, which let you add new bindings to a map or change existing bindings.

### Removals
`-` and `--`, which remove bindings from a map.

### Subcollection producers
`keys`, `keySet`, `keysIterator`, `valuesIterator`, and `values`, which return a `Map's` keys and values separately in
various forms.

### Transformations
`filterKeys` and `mapValues`, which produce a new map by filtering and transforming bindings of an existing map.


## Operations in trait immutable Map in detail

```scala
// Lookups:
ms get k // The value associated with key k in map ms as an option, or None if not found

ms(k)    // (or, written out, ms apply k) The value associated with key k in map ms, or a thrown exception if not found

ms getOrElse (k, d) // The value associated with key k in map ms, or the default value d if not found

ms contains k // Tests whether ms contains a mapping for key k

ms isDefinedAt k // Same as contains

// Additions and updates:

ms + (k -> v) // The map containing all mappings of ms as well as the mapping k -> v from key k to value v

ms + (k -> v, l -> w) // The map containing all mappings of ms as well as the given key/value pairs

ms ++ kvs // The map containing all mappings of ms as well as all key/value pairs of kvs

ms updated (k, v) // Same as ms + (k -> v)

// Removals:

ms - k // The map containing all mappings of ms except for any mapping of key k

ms - (k, l, m)  //The map containing all mappings of ms except for any mapping with the given keys

ms -- ks // The map containing all mappings of ms except for any mapping with a key in ks

// Subcollections:

ms.keys // An iterable containing each key in ms

ms.keySet // A set containing each key in ms

ms.keysIterator // An iterator yielding each key in ms

ms.values  // An iterable containing each value associated with a key in ms

ms.valuesIterator // An iterator yielding each value associated with a key in ms

// Transformation:

ms filterKeys p // A map view containing only those mappings in ms where the key satisfies predicate p

ms mapValues f // A map view resulting from applying function f to each value associated with a key in ms
```

## Operations in trait mutable Map

```scala
// Additions and updates:

ms(k) = v // (or, written out, ms.update(k, v)) Adds mapping from key k to value v to map ms as a side effect,
          // overwriting any previous mapping of k

ms += (k -> v) // Adds mapping from key k to value v to map ms as a side effect and returns ms itself

ms += (k -> v, l -> w) // Adds the given mappings to ms as a side effect and returns ms itself

ms ++= kvs // Adds all mappings in kvs to ms as a side effect and returns msitself

ms put (k, v) // Adds mapping from key k to value v to ms and returns any value previously associated with k as an option

ms getOrElseUpdate (k, d) // If key k is defined in map ms, returns its associated value. Otherwise, updates ms with
                          // the mapping k -> d and returns d
// Removals:

ms -= k // Removes mapping with key k from ms as a side effect and  returns ms itself

ms -= (k, l, m) // Removes mappings with the given keys from ms as a side effect and returns ms itself

ms --= ks // Removes all keys in ks from ms as a side effect and returns msitself

ms remove k // Removes any mapping with key k from ms and returns any value previously associated with k as an option

ms retain p // Keeps only those mappings in ms that have a key satisfying predicate p.

ms.clear() // Removes all mappings from ms

// Transformation and cloning:

ms transform f // Transforms all associated values in map ms with function f

ms.clone // Returns a new mutable map with the same mappings as ms
```

The addition and removal operations for maps mirror those for `Sets`. As for `Sets`, mutable `Maps` also support the
non-destructive addition operations `+`, `-`, and `updated`, but they are used less frequently because they involve a
copying of the mutable map. Instead, a mutable map `m` is usually updated "in place," using the two variants 
```scala 
m(key) = value or m += (key -> value).
```
There is also the variant `m put (key, value)`, which returns an `Option` value that contains the value previously
associated with key, or `None` if the key did not exist in the map before. The `getOrElseUpdate` is useful for accessing
maps that act as caches.

## Mutable Maps for memoization

Say you have an expensive computation triggered by invoking a function f:

```scala
def f(x: String) = {
  println("taking my time.");
  Thread.sleep(100)
  x.reverse }
```
Assume further that `f` has no side-effects, so invoking it again with the same argument will always yield the same
result. In that case you could save time by storing previously computed bindings of argument and results of `f` in a
`Map`, and only computing the result of `f` if a result of an argument was not found there. You could say the `Map` is a
cache for the computations of the function `f`.

```scala
val cache = collection.mutable.Map[String, String]()

//You can now create a more efficient caching version of the f function:
def cachedFunction(s: String) = cache.getOrElseUpdate(s, f(s))

cachedFunction("abc")
//taking my time.

cachedFunction("abc")
```

Note that the second argument to getOrElseUpdate is "by-name," so the computation of `f("abc")` above is only performed
if `getOrElseUpdate` requires the value of its second argument, which is precisely if its first argument is not found
in the cache map. You could also have implemented `cachedFunction` directly, using just basic `Map` operations, but it
would have taken more code to do so:

```scala
def cachedFunction(arg: String) = cache get arg match {
  case Some(result) => result
  case None =>
    val result = f(arg)
    cache(arg) = result
    result
}
```
