Link Checker
============

Run the master:

~~~
$ sbt
> runMain akka.Main reactive.ClusterMain
~~~

Run the workers (from different machines):

~~~
$ sbt
> runMain akka.Main reactive.ClusterWorker
~~~
