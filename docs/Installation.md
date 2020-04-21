## Setup Your Machine:
In order to work on the programming assignments, you need to have the following tools installed on your machine:

* Java Development Kit, JDK, version 1.8 (it is possible to use a higher version but not recommended)
* Scala build tool, SBT, version 0.13.17 or higher
* IntelliJ (a great Scala/Java IDE, but you are free to use whatever you want)


### Installing the JDK

Check you have the right version of the JDK by typing in the console:
Run `javac -version`
If you see something like:
```bash
$ javac -version
javac 1.8.0_241
```

Move to the next section.  Otherwise you'll need to install 1.8 version of the oracle JDK

Follow the [setup instructions](http://www.oracle.com/technetwork/java/javase/downloads/jre8-downloads-2133155.html) to download and install. Once the installation is complete, very the installation by running the following command in a terminal session:


Alternatively if you know you won't need the 1.8 version of the JDK and if you are running OSX and a [Homebrew Cask](https://github.com/caskroom/homebrew-cask) user, from a terminal run:

```bash
$ brew cask install java
```


### Installing sbt
Check to see if sbt is already installed:

```bash
$ sbt sbtVersion
...
[info] 0.13.18
```

If you need to install and you are running OSX and a [Homebrew](http://brew.sh/) user, from a terminal run:

```bash
$ brew install sbt@0.13
```

Otherwise follow the [setup instruction](http://www.scala-sbt.org/0.13/docs/index.html) to download and install. Once the installation is complete, verify the installation by running the following command in a terminal session:

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

[Here](https://stackoverflow.com/questions/38973049/how-to-install-scala-plugin-for-intellij) are some more hints on installing the plugin
