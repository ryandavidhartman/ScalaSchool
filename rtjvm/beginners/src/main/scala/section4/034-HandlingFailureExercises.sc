

import scala.util.{Random, Try}
///
// EXERCISES:
///

// Given these values and functions:

val hostname = "localhost"
val port = "8080"
def renderHTML(page: String) = println(page)

class Connection {
  def get(url: String): String = {
    val random = new Random(System.nanoTime())

    if(random.nextBoolean())
      "<html>...</html>"
    else
      throw new RuntimeException("Connection interrupted")
  }
}

object HttpService {

  def getConnection(host: String, port: String): Connection = {
    val random = new Random(System.nanoTime())
    if(random.nextBoolean())
      new Connection()
    else
      throw new RuntimeException("Connection unavailable")
  }
}

// Try to print the "html" page print it by calling renderHTML method

def maybeRender(url: String): Try[Unit] = Try { renderHTML(HttpService.getConnection(hostname, port).get(url))}
maybeRender("some page")


// Now what if we wanted to make the APIs in Connection and HttpService safe?  But we didn't
// have access to the source code to modify it.

// One solution is to use type classes!



// CODE YOU WRITE TO WRAP THE UNSAFE APIs

trait HasASafeGet[T] {
  def safeGet(c: Try[T])(url: String): Try[String]
}

object HasASafeGetInstances {
  implicit val connectionWithSafeGet = new HasASafeGet[Connection] {
    def safeGet(connection: Try[Connection])(url: String): Try[String] =
      connection.map(_.get(url))
  }
}

object HasASafeGetSyntax {
  implicit class HasASafeGetOps[T](value: Try[T]) {
    def safeGet(url: String)(implicit hasASafeGetInstance: HasASafeGet[T]): Try[String] = {
      hasASafeGetInstance.safeGet(value)(url)
    }
  }
}

object SafeHttpService {
  def getSafeConnection(host: String, port: String): Try[Connection] = Try {
    HttpService.getConnection(hostname, port)
  }
}

// How your clients use the safe APIs

import HasASafeGetSyntax.HasASafeGetOps
import HasASafeGetInstances.connectionWithSafeGet
import SafeHttpService.getSafeConnection

val connection = getSafeConnection(hostname, port)
val tryToGet = connection.safeGet("some url")




