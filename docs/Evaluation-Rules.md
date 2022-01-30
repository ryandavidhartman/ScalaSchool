## Evaluation Rules for variables

<!-- code -->
```scala
    // expression is evaluated whenever example1 is called
    def example1 = {expression}      
    
    // expression is evaluated immediately when example2 is declared its value copied once
    val example2 = {expression}

    // expression is evaluated once, but not until example3 is used the first time     
    lazy val example3 = {expression} 
```


## Evaluation Rules for function arguments

- Call by value: evaluates the function arguments before calling the function
- Call by name: evaluates the function first, and then evaluates the arguments if need be
- Variable numbers of args are also possible

## Call by Value
A by-value parameter is like receiving a val field; its body is evaluated once, when the
parameter is bound to the function's formal parameter 

Remember in the case of passing in an object that the formal function parameter is "bound" to
the original object. This means if your function mutates any fields in the object you will be
changing the variables of the original object!

*Think* passing in a pointer!

## Call by Name
This is a bit like passing in a def method; its body is evaluated whenever it is used inside
the function, if ever.

*Think* passing in a method that is evaluated each time it is used

<!-- code -->
```scala
    // call by value.  In the function we replace x with the value of the expression passed after evaluation
    def square(x: Double)
 
    // call by name.  In the function we replace x with the expression itself that we passed in.
    def square(x: => Double)

   // bindings is a sequence of int, containing a varying # of arguments 
   def myFct(bindings: Int*) = { ... } 
```


![CBVvsCBN](./imgs/rtjvmCBVvsCBN.png) 
