# Recursive Functions

A function is *recursive* if it calls itself.  If the only place the function calls
itself is the last expression of the function, then the function is called *tail
recursive*.

Recursive functions are very popular in functional programming because they offer a
way to iterate over data structures or calculations without using mutable data.

Here is an example of a recursive function that raises an integer by a given positive
exponent:

```scala
def power(x: Int, exp: Int): Long = {
  if(exp <  1)
   1
 else
   x * power(x,exp-1)
}
```

## tail recursion

One problem with using recursive functions is running into the dreaded "Stack Over-flow"
error, where invoking a recursive function many times eventually uses up all of the 
allocated stack space.

To prevent this scenario, the Scala compiler can optimize some recursive functions with
*tail recursion* so that recursive calls do not use additional stack space.  With
tail-recursion-optimized functions recursive invocation doesn't create new stack space but
instead used the current function's stack space.  Only functions whose last expression is
the recursive invocation can be optimized for tail-recursion by the Scala compiler.  If
the result of invoking itself is used for anything but the direct return value , a function
can't be optimized.

Fortunately there is a *function annotation* available to mark a function as being intended
to be optimized for tail-recursion.  A function marked with the tail-recursion function 
annotation will cause an error at compilation time if it cannot be optimized for tail recursion.

```scala
@annotation.tailrec
def power(x: Int, exp: Int): Long = {
  if(exp <  1)
   1
 else
   x * power(x,exp-1)
}

ERROR: could not optimize @tailrec annotated method power: it contains a recursive call
not in tail position x * power(x,exp-1)
                     ^
Compilation Failed
```

## Using tail recursion

Hmm, in the last example above, the recursive call *is* the last item in the function.  What
is wrong?  Ah!  We're taking the result of the recursive call and multiplying it by a value,
so that multiplication is actually the last expression evaluation in the function, not the 
recursive call. 

The standard way of fixing this is with the use of an *accumulator* to store the value of the 
computation:

```scala
@annotation.tailrec
def power(x: Int, exp: Int, acc: Long = 1L): Long = {
  if(exp <  1)
   acc
 else
   power(x, exp-1, x*acc)
}
```

## Performance: Loops vs tail recursion

Consider the following class with two methods for printing the first 10 positive integers to the
screen:

```scala

class Looper {
   def l1 = {
    var i = 1
    while(i <= 10) {
      println(i)
      i = i + 1
    }
  }

  def l2(i: Int = 1): Unit  = {
    if(i <= 10) {
      println(i)
      l2(i+1)
    }
  }

}
```

Will there be any performance difference in `l1` and `l2`?

Here is the byte code for `l1`:

```
  public void l1();
    Code:
       0: iconst_1
       1: istore_1
       2: iload_1
       3: bipush        10
       5: if_icmpgt     25
       8: getstatic     #21                 // Field scala/Predef$.MODULE$:Lscala/Predef$;
      11: iload_1
      12: invokestatic  #27                 // Method scala/runtime/BoxesRunTime.boxToInteger:(I)Ljava/lang/Integer;
      15: invokevirtual #31                 // Method scala/Predef$.println:(Ljava/lang/Object;)V
      18: iload_1
      19: iconst_1
      20: iadd
      21: istore_1
      22: goto          2
      25: return
```

and here is the byte code for `l2()`:

```
  public void l2(int);
    Code:
       0: iload_1
       1: bipush        10
       3: if_icmpgt     26
       6: getstatic     #21                 // Field scala/Predef$.MODULE$:Lscala/Predef$;
       9: iload_1
      10: invokestatic  #27                 // Method scala/runtime/BoxesRunTime.boxToInteger:(I)Ljava/lang/Integer;
      13: invokevirtual #31                 // Method scala/Predef$.println:(Ljava/lang/Object;)V
      16: aload_0
      17: iload_1
      18: iconst_1
      19: iadd
      20: invokevirtual #39                 // Method l2:(I)V
      23: goto          26
      26: return
```

So the only performance difference is that of a `goto` vs `function invocation`





