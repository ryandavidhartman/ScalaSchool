package section6.commands
import section6.filesystem.State

class RmDir(name: String) extends RmEntry(name) {
  override def doRemove(): Unit = ???
}
