##Definition
Higher order functions, _HOFs_, are functions that accept a function(s) as input parameter(s) 
or return a function.

<!-- code -->
```scala
    // Example 1 
    // map() takes a function and a seq as parameters, and returns a new seq which is 
    // the original sequence with the function applied to each elem
    
    def map[T,U](f: (T) => U, seq: Seq[T]): Seq[U] = {
      for {
        x <- seq
      } yield f(x)
    }

    // Example 2
    // sum() returns a function that takes two integers and returns an integer  
    def sum(f: Int => Int): (Int, Int) => Int = {  
      val sumf = (a: Int, b: Int) => { f(a) + f(b) }  
      sumf  
    } 

    // Example 3
    // same as above. Its type is (Int => Int) => (Int, Int) => Int  
    def sum(f: Int => Int)(a: Int, b: Int): Int = { (f(a) + f(b) } 

    // Called like this
    sum((x: Int) => x * x * x)          // Anonymous function, i.e. does not have a name  
    sum(x => x * x * x)                 // Same anonymous function with type inferred

    def cube(x: Int) = x * x * x  
    sum(x => x * x * x)(1, 10) // sum of cubes 1 & 10
    sum(cube)(1, 10)           // same as above      
```

##Notes on methods
HOFs which are written to take *functions* as input parameters will accept *methods* as input, since
methods can in general be converted to a function via an eta expansion.

![HOF](imgs/rtjvmHigherOrderFunctions.png)