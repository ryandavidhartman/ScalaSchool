import sbt.Keys.version

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "com.example",
      scalaVersion := "2.13.3"
    )),
    name := "beginners",
    version := "0.1"
  )




libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.2" % Test
