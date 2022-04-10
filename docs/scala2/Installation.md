## Setup Your Machine:
In order to work on the programming assignments, you need to have the following tools installed on your machine:

* Java Development Kit, JDK, version *11* (Scala 2.13 supports Java 8 to 15)
* Scala build tool, SBT, version 1.4.x (But any 1.x version will be fine)
* IntelliJ (a great Scala/Java IDE, but you are free to use whatever you want)


### Installing the JDK

Check you have the right version of the JDK by typing in the console:
Run `javac -version`
If you see something like:
```bash
$ javac -version
javac 11.0.10
```

Move to the next section.  Otherwise you'll need to install 11 version of the oracle JDK


If you are running OSX and a [Homebrew Cask](https://github.com/caskroom/homebrew-cask) user, from a terminal run:


```bash
$  brew cask install adoptopenjdk/openjdk/adoptopenjdk11
```

[Here](https://github.com/AdoptOpenJDK/homebrew-openjdk/blob/master/README.md) is more info on installing the adopt open jdk

or if you prefer to, you can get the installer from Oracle

Follow the [setup instructions](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html) to download and install. Once the installation is complete, very the installation by running the following command in a terminal session:

### Installing sbt
Check to see if sbt is already installed:

```bash
$ sbt sbtVersion
...
[info] 1.4.7
```

If you need to install and you are running OSX and a [Homebrew](http://brew.sh/) user, from a terminal run:

```bash
$ brew install sbt
```

Other wise follow the [setup instruction](https://www.scala-sbt.org/1.x/docs/Setup.html) to download and install. Once the installation is complete, verify the installation by running the following command in a terminal session:

Summary of important sbt commands
* compile - builds the project
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

[Here](https://stackoverflow.com/questions/38973049/how-to-install-scala-plugin-for-intellij) are some more hints on installing the plugin

### Official Scala Docs on Getting Started:

[docs.scala-lang.org](https://docs.scala-lang.org/getting-started/)
