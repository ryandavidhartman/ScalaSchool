trait Logger {
  def log(msg: String): Unit
  def info(msg: String): Unit = log(s"Info: $msg")
}

trait TimeStampLogger extends Logger {
  abstract override def log(msg: String): Unit = {
    super.log(s"${System.currentTimeMillis}: $msg")
  }
}

trait ConsoleLogger extends Logger {
  override def log(msg: String): Unit = println(msg)
}

object App extends ConsoleLogger with TimeStampLogger


App.log("what will happen?")

App.info("how about now?")