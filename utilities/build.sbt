lazy val root = (project in file("."))
  .settings(
    name := "Utilities",
    scalaVersion := "2.13.3"
  )

libraryDependencies += "org.scalatest" %% "scalatest-funsuite" % "3.2.10" % "test"