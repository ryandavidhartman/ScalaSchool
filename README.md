## Setup Your Machine:
In order to work on the programming assignments, you need to have the following tools installed on your machine:

* Oracle JDK, the Java Development Kit, version 1.8 or higher
* Scala build tool (sbt)

Check you have the right version of the JDK by typing in the console:
`java -version`

Check to see if you have sbt insalled
`sbt sbtVersion`

### Installing the JDK

If you are running OSX and a [Homebrew Cask](https://github.com/caskroom/homebrew-cask) user, from a terminal run:

```bash
$ brew cask install java
```

Otherwise follow the [setup instructions](http://www.oracle.com/technetwork/java/javase/downloads/jre8-downloads-2133155.html) to download and install. Once the installation is complete, very the installation by running the following command in a terminal session:

```bash
$ java -version
java version "1.8.0_151"
Java(TM) SE Runtime Environment (build 1.8.0_151-b12)
Java HotSpot(TM) 64-Bit Server VM (build 25.151-b12, mixed mode)
```

### Installing sbt
If you are running OSX and a [Homebrew](http://brew.sh/) user, from a terminal run:

```bash
$ brew install sbt
```

Otherwise follow the [setup instruction](http://www.scala-sbt.org/0.13/docs/index.html) to download and install. Once the installation is complete, verify the installation by running the following command in a terminal session:

```bash
$ sbt sbtVersion
...
[info] 1.2.7
```

Summary of important sbt commands

* exit - ends the sbt session
* help - lists the available commands
* compile - compiles the main sources
* test:compile - compiles the test and main sources
* test - runs tests after compiling the test and main sources
* console - starts the REPL with the project on the classpath
* run - runs a main class, passing args provided on command line
* clean - deletes files produced by the build in the target directory
* reload - reloads the project in the current sbt session
* sbtVersion - provides the version of sbt


### Installing IntelliJ

Download and install [IntelliJ IDEA Community Edition](https://www.jetbrains.com/idea/download)

After starting IntelliJ IDEA use the **Next:..** button on the **Customize IntelliJ IDEA** wizard to advance to Featured plugins
**Install** Scala plugin




## Online Scala Resources:
* [A Tour of Scala: Tutorial introducing the main concepts of Scala](https://docs.scala-lang.org/tour/tour-of-scala.html)
* [Scala Tutorial (another quick reference with content written for beginners)](https://www.tutorialspoint.com/scala/)
* [Scala-Lang.org: Scala's main page](https://www.scala-lang.org/)
* [Scala Standard Library API](https://www.scala-lang.org/api/current/)
* [LightBend (Scala, Akka, Play enterprise support)](https://www.lightbend.com/)
* [Scala Overview on StackOverflow: A list of useful questions sorted by topic](http://stackoverflow.com/tags/scala/info)
* [A Scala tutorial from Twitter](http://twitter.github.io/scala_school/)
* [Scala training from 47 Degrees](https://www.scala-exercises.org/)
* [Scala training from Data Flair](https://data-flair.training/blogs/scala-tutorial/)

## Scala Books
* [Programming in Scala book (my personal recommendation for an intro to Scala)](https://people.cs.ksu.edu/~schmidt/705a/Scala/Programming-in-Scala.pdf)
* [Scala for the Impatient (an alternative to Programming in Scala for those with Java experience)](https://fileadmin.cs.lth.se/scala/scala-impatient.pdf)
* [Functional Programming in Scala (for those who want a rigorous approach to learning functional programming with Scala)](https://www.manning.com/books/functional-programming-in-scala)

## Online Programming Tools:
* [Scastie](https://scastie.scala-lang.org/)
* [Scala Fiddle](https://scalafiddle.io/)
* [Scala Meta AST Explorer](https://astexplorer.net/#/gist/ec56167ffafb20cbd8d68f24a37043a9/677e43f3adb93db8513dbe4e2c868dd4f78df4b3)

## Cheat Sheet
[List of basic Scala concepts](https://github.com/ryandavidhartman/ScalaSchool/wiki)
