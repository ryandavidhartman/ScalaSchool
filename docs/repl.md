# Scala REPL

Scala has an interactive interpreter, usually referred to as the *REPL* (Read, Evaluate, Print Loop). To start the REPL, at the shell prompt, enter scala:

```bash
ryandavidhartman@DESKTOP-HKJ042P:~$ scala
Welcome to Scala 2.13.3 (OpenJDK 64-Bit Server VM, Java 11.0.10).
Type in expressions for evaluation. Or try :help.

scala>
```

If you have used other REPL shells like Python's python, Ruby's irb, or Groovy's groovysh you'll find the Scala REPL familiar.  As with the REPL's provided by Python, Ruby, and Groovy runtimes Scala's REPL provides support for evaluating and executing code one line at a time with helpful feedback.

At the REPL prompt, you can enter a Scala expression. The expression is evaluated, and the result is assigned automatically to a fresh result variable as well as printed to the console. For example:

```scala
scala> 3 * 7
res0: Int = 21

scala> res0 * 2
res1: Int = 42
```

## Useful REPL Commands

At the REPL prompt, you can enter and evaluate any Scala expression. Additionally, the REPL has a set of utility commands, all starting with a : character. To see a complete list, enter :help. Some of the more commonly used REPL commands are:

* :help [command] - get help!
* :load – Read and evaluate Scala commands from a file
* :reset – Reset the REPL to initial state, deleting all variables, etc.
* :quit – I’m outta here! <Control+D> works as well.
* :paste - Lets you paste in multiple lines of code
* :paste -raw - paste with code wrapping disabled

## Alternative REPLs

* [Ammonite](https://ammonite.io/#Ammonite-REPL) an advanced Scala REPL
* [Scala](https://www.jetbrains.com/help/idea/run-debug-configuration-scala-console.html) REPL from inside IntelliJ
* [Scala Worksheets]()https://www.jetbrains.com/help/idea/work-with-scala-worksheet-and-ammonite.html from inside IntelliJ
