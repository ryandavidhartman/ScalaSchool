import scala.util.Random

/*
Exercise 1
You are given some configuration and connection class and companion objects defined below.

 */

val config: Map[String, String] = Map(
  // fetched from elsewhere, and may or may not be present
  "host" -> "176.45.36.1",
  "port" -> "443"
)

class Connection {
  def connect = "Connected successfully"  // this connects to some server
}

object Connection {
  val random = new Random(System.nanoTime)
  def apply(host: String, port: String): Option[Connection] = {
    // ok so we we randomly make either a successful or failed connection
    if(random.nextBoolean())
      Some(new Connection)
    else
      None
  }
}

def connectionStatus():Option[String] = for {
  host <- config.get("host")
  port <- config.get("port")
  connection <- Connection(host, port)
} yield connection.connect

(1 to 10).foreach(_ => println(s"Status: ${connectionStatus()}"))