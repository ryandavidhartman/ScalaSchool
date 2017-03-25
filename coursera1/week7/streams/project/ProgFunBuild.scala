import sbt._
import Keys._
import ch.epfl.lamp.CourseraBuild
import ch.epfl.lamp.SbtCourseraPlugin.autoImport._

object ParprogBuild extends CourseraBuild {

  lazy val instrumentedClassPrefixes = SettingKey[Seq[String]]("instrumentedClassPrefixes")

  override def assignmentSettings: Seq[Setting[_]] = Seq(
    // This setting allows to restrict the source files that are compiled and tested
    // to one specific project. It should be either the empty string, in which case all
    // projects are included, or one of the project names from the projectDetailsMap.
    currentProject := "",

    commonSourcePackages := Seq("common"),

    libraryDependencies += "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.4",

    // Files that we hand out to the students
    handoutFiles <<= (baseDirectory, projectDetailsMap, commonSourcePackages) map {
      (basedir, detailsMap, commonSrcs) =>
      (projectName: String) => {
        val details = detailsMap.getOrElse(projectName, sys.error("Unknown project name: "+ projectName))
        val commonFiles = (PathFinder.empty /: commonSrcs)((files, pkg) =>
          files +++ (basedir / "src" / "main" / "scala" / pkg ** "*.scala")
        )
        (basedir / "src" / "main" / "scala" / details.packageName ** "*.scala") +++
        commonFiles +++
        (basedir / "src" / "main" / "resources" / details.packageName ** "*.txt") +++
        (basedir / "src" / "test" / "scala" / details.packageName ** "*.scala") +++
        (basedir / "build.sbt") +++
        (basedir / "project" / "build.properties") +++
        (basedir / "project" ** ("*.scala" || "*.sbt")) +++
        (basedir / "project" / "scalastyle_config.xml") +++
        (basedir / "lib_managed" ** "*.jar") +++
        (basedir * (".classpath" || ".project")) +++
        (basedir / ".settings" / "org.scala-ide.sdt.core.prefs")
      }
    },

    // Instrumentation
    libraryDependencies += "ch.epfl.lamp" %% "scala-grading-runtime" % "0.3",
    addCompilerPlugin("ch.epfl.lamp" % "scala-grading-plugin" % "0.3" cross CrossVersion.full),
    instrumentedClassPrefixes := Seq(),
    scalacOptions ++= {
      for (prefix <- instrumentedClassPrefixes.value)
        yield "-P:scalagrading:instrumentClassPrefix:" + prefix
    }
  )

}
