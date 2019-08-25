## Setup Your Machine:
In order to work on the programming assignments, you need to have the following tools installed on your machine:

Oracle JDK, the Java Development Kit, version 1.8. Note some assignments may work with Java 1.7, but we grade the assignments with 1.8, so we strongly recommend to use Java 1.8. Check you have the right version by typing in the console:
java -version
Scala Build Tool (sbt), a build tool for Scala, version 0.13.x, or newer.
The Scala IDE for Eclipse, Intellij IDEA or another IDE of your choice.
Please follow the instructions on this page carefully.

### Installing the JDK
Linux
Ubuntu, Debian: To install the JDK using apt-get, execute the following command in a terminal sudo apt-get install openjdk-8-jdk
Fedora, Oracle, Red Had: To install the JDK using yum, execute the following command in a terminal su -c "yum install java-1.8.0-openjdk-devel"
Manual Installation: To install the JDK manually on a Linux system, follow these steps:

1. Download the .tar.gz archive from the Oracle website

2. Unpack the downloaded archive to a directory of your choice

3. Add the bin/ directory of the extracted JDK to the PATH environment variable. Open the file ~/.bashrc in an editor (create it if it doesn't exist) and add the following line:

export PATH="/PATH/TO/YOUR/jdk1.8.0-VERSION/bin:$PATH"
If you are using another shell, add that line in the corresponding configuration file (e.g. ~/.zshrc for zsh).

Verify your setup: Open a new terminal (to apply the changed ~/.bashrc in case you did the manual installation) and type java -version. If you have problems installing the JDK, ask for help on the forums.

Mac OS X
Mac OS X either comes with a pre-installed JDK, or installs it automatically.

To verify your JDK installation, open the Terminal application in /Applications/Utilities/ and type java -version. If the JDK is not yet installed, the system will ask you if you would like to download and install it. Make sure you install Java 1.8.

Windows
Download the JDK installer for Windows from the Oracle website.
Run the installer.
Add the bin directory of the installed JDK to the PATH environment variable, as described here.
To verify the JDK installation, open the Command Prompt and type  𝑗𝑎𝑣𝑎−𝑣𝑒𝑟𝑠𝑖𝑜𝑛 . If you run into any problem, go to the official Oracle documentation.

### Installing sbt
Follow the instructions for your platform to get it running.

This course requires sbt version >0.13.x. If you have previously installed sbt 0.12.x, you need to uninstall it and install a newer version. sbt 0.13.x and newer versions can be used for projects and other courses requiring sbt 0.12.x, but not the other way around. If in doubt, you can check your currently installed sbt like this: in an arbitrary directory that is not a programming assignment or otherwise an sbt project, run:

sbt about
You should see something like this:

[info] This is sbt 1.1.0
If the sbt command is not found, you need to install sbt. 


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
