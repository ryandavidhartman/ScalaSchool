name := "BuildToolDemo"

version := "1.0"

scalaVersion := "3.0.2"

libraryDependencies ++= List(
  "com.lihaoyi" %% "fansi" % "0.2.14",
  "org.scalameta" %% "munit" % "0.7.29" % Test
)

testFrameworks += TestFramework("munit.Framework")

makeSite / mappings := {
  val indexFile = target.value / "index.html"
  IO.write(indexFile, "<h1>Hello from sbt!</h1>")
  Seq(indexFile -> "index.html")
}
