# Futures

## Introduction

Scala provides _ scala.concurrent.Future_ to encode delayed operations. A Future is a handle for a value not yet
available. Futures are commonly used to return values for its asynchronous APIs. A synchronous API waits for a
result before returning; an asynchronous API does not. For example, an HTTP request to some service on the internet
might not return a value for half a second. You don’t want your program’s execution to block for half a second waiting.
“Slow” APIs can return a Future right away and then “fill in” its value when it resolves.

```scala
val myFuture = MySlowService(request) // returns right away
   ...do other things...
val serviceResult = Await.result(myFuture) // blocks until service "fills in" myFuture
```
In practice, you won’t write code that sends a request and then calls _Await.result()_ a few statements later! A Future
has methods to register callbacks to invoke when the value becomes available.

If you’ve used other asynchronous APIs, you perhaps cringed when you saw the word “callbacks” just now. You might
associate them with illegible code flows, functions hiding far from where they’re invoked. But Futures can take
advantage of Scala’s first-class functions to present a more-readable code flow. You can define a simpler handler
function in the place where it’s invoked.

For example to, write code that dispatches a request and then “handles” the response, you can keep the code together:

```scala
val future = dispatch(req) // returns immediately, but future is "empty"
future onSuccess { reply => // when the future gets "filled", use its value
  println(reply)
}
```

## Using the REPL

You can play with Futures in the REPL. This is a bad way to learn how you will use them in real code, but can help with
understanding the API. When you use the REPL, _Promise_ is a handy class. It’s a concrete subclass of the abstract
Future class. You can use it to create a Future that has no value yet.

```scala
@ import scala.concurrent.{Future, Promise, Await}
import scala.concurrent.{Future, Promise, Await}

// More on this later!
@ implicit val ec: scala.concurrent.ExecutionContext = scala.concurrent.ExecutionContext.global
ec: concurrent.ExecutionContext = scala.concurrent.impl.ExecutionContextImpl$$anon$3@41c89d2f[Running, parallelism = 12, size = 0, active = 0, running = 0, steals = 0, tasks = 0, submissions = 0]

@ import scala.concurrent.duration._
import scala.concurrent.duration._

@ val f6 = Future(6)
f6: Future[Int] = Future(Success(6))

@ Await.result(f6, Duraiton.Inf)
6

@ val pr7 = Promise[Int]() // create unresolved future
pr7: Promise[Int] = Future(<not completed>)
  
@ Await.result(pr7.future, DurationInt(10).seconds)
  java.util.concurrent.TimeoutException: Future timed out after [10 seconds]
  scala.concurrent.impl.Promise$DefaultPromise.tryAwait0(Promise.scala:248)
  scala.concurrent.impl.Promise$DefaultPromise.result(Promise.scala:261)
  scala.concurrent.Await$.$anonfun$result$1(package.scala:201)
  scala.concurrent.BlockContext$DefaultBlockContext$.blockOn(BlockContext.scala:62)
  scala.concurrent.Await$.result(package.scala:124)
  ammonite.$sess.cmd8$.<clinit>(cmd8.sc:1)

 @ completedFuture = Future(1)   
 @ pr7.completeWith(completedFuture)
    res12: Promise[Int] = Future(Success(1))
    
 @  Await.result(pr7.future, DurationInt(10).seconds)
    res13: Int = 1
```
When you use Futures in real code, you normally don’t call Await.result(); you use callback functions instead.
Await.result() is just handy for REPL tinkering and unit tests.
