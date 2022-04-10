# sbt - The interactive build tool

This is an introduction to [sbt](https://www.scala-sbt.org/), which is a common build tool for Scala projects. While
sbt is a very popular build tool for Scala, there are several other build tools which support Scala:

* [Maven](https://maven.apache.org/index.html)
* [Gradle](https://gradle.org/)
* [Mill](https://github.com/com-lihaoyi/mill),

and so on. Before we get started, what exactly is a build tool?

## What is a build tool?
A "Build tool" is a catch-all term that refers to anything that is needed to get a piece of software set up,
but isn't needed after that.

### Developer Workflow
Let's begin with an overview of how build tools manage your workflow.  As a developer, when you work on the program,
you need to perform various tasks, such as compiling the sources, running the program, running the tests or deploying
the program. And some of these tasks may also be performed by a continuous integration server or maybe automatically
triggered on source changes. So we want a way to automate these tasks.  A build tool provides that automation. 

![Developer Workflow](./imgs/effective-build-tool1.png)

### Compiling
What is it to compile a Scala program? It consists of invoking the Scala compiler on all the source files to turn them into 
executable JVM bytecode.  But first it requires constructing the class pass of the application by resolving and 
fetching all its library dependencies. And possibly it may also require generating parts of the source code or 
resources like assets, data types and serializers.

![Compiling](./imgs/effective-build-tool2.png)

### Running
Running the program or, its test, consists of building an execution environment, ( e.g. a JVM with the correct
classpath) and invoking the program entry point. But first it requires compiling the program.  So in terms of a build
tool we say the run task, depends on the compilation task.

![Running](./imgs/effective-build-tool3.png)

### Deploying and Publishing
Deploying or publishing a program can take various forms. It can consist of publishing an artifact to a library
repository or packaging the program, and it's dependencies into a single jar file and so on. But first it requires
compiling the program

![Deploying](./imgs/effective-build-tool4.png)

### Build Tools
Manually invoking all these tasks, compiling fetching dependencies, etc. and in the right holder is cumbersome.
Instead, build tools can manage this task for you. The idea is that you indicate to the build tool where your source
files are located. Which library of program depends on etc. And the build tool takes care of fetching the dependencies,
invoking the compiler, running the test, etc.

![Build Tools](./imgs/effective-build-tool5.png)

### Build Tools as a Task Engine
Generally build tools are not limited to a specific developer workflow, but they implement a generic task engine able
to coordinate the execution of a graph of tasks like this one (below). We have a task run, which depends on a task 
compile, which depends on fetch. To summarize. Working on a program involves performing values interdependent tasks
such as compiling, running and deploying the program. Build tools aim at simplifying the coordination of this task.


![Task Engine](./imgs/effective-build-tool6.png)

## Creating a sbt project
Now that we know, in general, what a build tool is let us move on to discussing SBT, which is our build tool of
choice for Scala projects.  Let's get started with our first SBT project using both SBT and IntelliJ. 

### Install sbt and make en empty project
Before you start, make sure the sbt command-line tool is installed according to the
[instructions]((./Installation.md)). Also Java, and IntelliJ!

### First Project
Ok to make an sbt project you need a directory with the following two files, _build.properties_,
located in a subdirectory called _project_, and a top-level filed called _build.sbt_.

Recap:

![Sbt Hello](./imgs/effective-build-tool8.png)


This video will walk you through making a simple sbt project.

[![Watch the video](./imgs/effective-build-tool-video1.png)](https://user-images.githubusercontent.com/1116629/127788461-137f0a7b-fd06-4127-8daf-9c56bfb8903f.mp4)

 
### Main Sbt concepts: settings and tasks

In the previous video we saw how to invoke the tasks _compile_ and _run_. In the diagram below, I 
want to illustrate the fact that the task _run_ depends on the task _compile_. So if I invoke run
without first invoking compile, sbt automatically invokes compile first.

Let's introduce a few other tasks. For instance, _compile_ depends on _update_, which resolves the
library dependencies of the project. This task depends on a setting, named _libraryDependencies_.
By changing the value of this setting, you change the libraries that are added to your project.

So there are two main concepts in sbt, _settings_ and _tasks_. Settings parameterize the build, and
they are evaluated only once, when sbt loads the project. And tasks perform actions such as
downloading the dependencies or running the program. They are evaluated each time you invoke them.
So each time you invoke the task run, it runs your program.

Tasks can be parameterized by settings' values, and by other tasks' results. For instance, the
setting libraryDependencies defines the libraries that your project depends on. And the task update
downloads these dependencies when you invoke the task. 

![Main Sbt Concepts](./imgs/effective-build-tool9.png)

### Adding new dependencies to your project

As ab example of adding library dependencies, let's see how to add a library, called 
_fansi_ for colorizing the text we print to the console, to your project.  Then we'll see how to add
a unit test library _munit_ to our project. 

[![Watch the video](./imgs/effective-build-tool-video2.png)](https://user-images.githubusercontent.com/1116629/129651593-e9354438-5775-4936-960c-f4de62ffc2c1.mp4)


### sbt plugins

sbt provides common predefined tasks and settings out of the box.  Additional tasks or predefined
configurations can be provided by plugins.

Let's see how you would use add an sbt plugin in your project. Plugins are dependencies to the build
definition itself, not to the program. They are declared in the file project/plugins.sbt, instead
of build.sbt.

As an example, let's install plugin that configures the compiler's reporter to present the errors
in a different way.


[![Watch the video](./imgs/effective-build-tool-video3.png)](https://user-images.githubusercontent.com/1116629/132779543-455e9c85-7724-4a2f-9c1b-3379df905620.mp4)

### Keys and Scopes

In this final section on sbt, we'll explain the concept of _scopes_ in sbt, and how to use them. We 
have already seen that _source_ directories are different for the program and its test. The source
files of the program are in the directory _scr/main/scala_, whereas the source file of the tests are in
_src/test/scala_.

However, there is a single _key_ (or setting) called _sourceDirectory_. But there is no such key as 
_testsourceDirectory_. How does the test task find the test sources?

It turns out that a single _key_ can have different values in different _scopes_. We can see that in the
sbt shell, if I query the sourceDirectory key in the compile scope, I get _src\main_, whereas in the test
scope I get _src\test_.

```sbt
sbt:BuildToolDemo> Compile/sourceDirectory
[info] /home/ryandavidhartman/dev/source/ScalaSchool/courseraClasses/EffectiveProgrammingInScala/lectures/week03/BuildToolDemo/src/main

sbt:BuildToolDemo> Test/sourceDirectory
[info] /home/ryandavidhartman/dev/source/ScalaSchool/courseraClasses/EffectiveProgrammingInScala/lectures/week03/BuildToolDemo/src/test
```

There is a single concept of source directory modeled by the key _sourceDirectory_ and re-used by both
the compile and test configurations. By scoping the key to the corresponding configuration, each key can
be assigned a value along with a configuration such as _Compile_, _Test_ or no specific configuration which
is named _Zero_.

When we look up the value of a key, we can specify the configuration we are interested in with the
syntax:

```sbt
Scope / Key
Compile/sourceDirectory (as an example)
```

If no configuration is specified. Sbt first tries with the _Compile_ configuration and falls back to
the _Zero_ configuration. For instance, just _run_ is equivalent to _Compile \ run _, which means run
in the _Compile_ configuration.

Conversely, if we look up for _Compile \ scalaVersion_ , which means
the value of the setting _scalaVersion_ in _Compile_ scope. That the key _scalaVersion_ has no value in
that scope. Then sbt falls back to a more generic scope: it looks up in the zero configuration,

Configurations are just one possible axis of key scoping. Keys can also have different values according
to a particular task key. For instance, the task _unmanagedSources_ lists all the projects source files.

```sbt
sbt:BuildToolDemo> show unmanagedSources
[info] * /home/ryandavidhartman/dev/source/ScalaSchool/courseraClasses/EffectiveProgrammingInScala/lectures/week03/BuildToolDemo/src/main/scala/hellosbt/HelloSbt.scala
```

If we invoke this task, we see that we have just one source file, HelloSbt.scala. These task can be
configured by changing the value of the setting, _includeFilter_ in the scope of the task
_unmanagedSources_, we can query the current value of the sitting by writing 
_unmanagedSources \ includeFilter_.

```sbt
sbt:BuildToolDemo> unmanagedSources / includeFilter
[info] ExtensionFilter(java,scala)
```

Here it says that sbt looks for source files with extensions that .java, and .scala

let's also include .sc files. To achieve this, we assign a new value to _includeFilter_ in the scope
of the _unmanagedSources task_ . 

In the build.sbt file we add:

```scala
unmanagedSources / includeFilter := new io.ExtensionFilter(
  "java",
  "scala",
  "sc"
)
```

then

```sbt
sbt:BuildToolDemo> unmanagedSources / includeFilter
[info] ExtensionFilter(java,scala, sc)
```


Finally, there is a third axis that can be used to assign values to sbt keys.

When a project contains sub-projects, each sub-project can set its own values for some keys. This is
typically the case for the setting _baseDirectory_, which defines the root directory of each sub-project.

In our build definition example, we only have one project, so all our settings are scoped to this
project. We can explicitly see that by prefixing the name of a key with the name of our project,
BuildToolDemo.

If I write _BuildToolDemo \ sourceDirectory_, I get the value of the setting sourceDirectory in the
scope of the project BuildToolDemo.

```sbt
sbt:BuildToolDemo> BuildToolDemo / sourceDirectory
[info] /home/ryandavidhartman/dev/source/ScalaSchool/courseraClasses/EffectiveProgrammingInScala/lectures/week03/BuildToolDemo/src
```

There is also a special project named _ThisBuild_, which means the "entire build", so a setting applies
to the entire build rather than just a single project.

Sbt falls back to _ThisBuild_ when you look for the value of a key that has not been defined for a
specific project.

This is a convenient way to define cross-project settings. For instance, to set the _scalaVersion_
for all the projects in _ThisBuild_ definition, you write _ThisBuild / scalaVersion_ 

In the build.sbt file we add:

```scala
ThisBuild / scalaVersion := "3.0.2"
```

Here are some examples of how to query the value of a key according to multiple axes.

```sbt
  // query includeFilter key in current project, no configuration, unmanagedSource task 
  unmanagedSources / includeFilter
  
  // query includeFilter key in the hello-world project, no configuration, unmanagedSource task
  hello-world / unmanagedSources / includeFilter

  // query includeFilter key in the hello-world project, compile configuration, unmanagedSource task
  hello-world / Compile / unmanagedSources / includeFilter
```

n summary, when the same concept, like a source directory is reused in several contexts such as
configurations, projects, or tasks, sbt in courageous you to use a single sitting key for this
concept and to scope the value you assign to it to the desired context.


## Sbt Review
sbt is an interactive build tool you terminal, and then you manage your project from there. The
build definition is written in Scala. A build definition essentially assignments values to setting
keys such as _scalaVersion_ or _libraryDependencies_.

