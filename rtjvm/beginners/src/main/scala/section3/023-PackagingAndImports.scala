package section3  // Here we use the section3 package

object PackagingAndImports extends App {

  //
  // Packages
  //

  // Since PackingAndImport and Writer are defined in the same package
  // we can use it here.  i.e. Members of the same package are available by
  // their simple names.
  val writer = new Writer("Daniel", "RockTheJVM", 2018)

  import Exercise023.Princess  // Here we import Princess class from its package
  val princess = new Princess

  // If you don't want to use an import statement you can use the class's FQND
  //val princess = new section3.Exercise023.Princess

  // packages are hierarchical, defined by the `.` notation
  // Normally the class hierarchy is matched by the file system

  //
  // Package Objects
  //

  // This is used to define "stuff" outside a class (or object definition).
  // We can put such things in a package object
  // There can only be 1 package object per name space and it must be named
  // the same as its package and it is defined in a file called package.scala

  import Exercise023._  // We import everything INCLUDE definitions in the package object
                        // from the Exercise023 package

  sayHello()
  println(SPEED_OF_LIGHT)

  //
  // Imports
  //

  //  import package_name.{A, B}  imports A and B from the package called `package_name`
  //  import package_name._ imports everything from the package called `package_name`
  //  import package_name.{A => B} imports A from the package called `package_name` and aliases it as B
  //  This can be useful if you want to import 2 classes that have the same name.

  import java.util.Date
  import java.sql.Date

  val javaDate = new Date() // this is a java date
  val sqlDate = new java.sql.Date(1223232L)  // you can get a SQL Date using a FQDN

  // or use an alias

  import java.sql.{Date => SqlDate}
  val sqlDate2 = new SqlDate(32323L)

  //
  // Default Imports
  //

  // All scala programs automatically include this default imports:
  // java.lang._  String, Object, Exception are form here
  // scala._  Int, Nothing, Function are from here
  // scala.Predef._  Println, ??? are from here


}
