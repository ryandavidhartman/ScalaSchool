package section6.commands
import section6.files.Directory
import section6.filesystem.State

class RmDir(name: String) extends RmEntry(name) {
  override  val failMessage: String => String = (d:String) => s"No such directory: $d"

  override val remover: Directory => String => Directory = (d: Directory) => (name: String) => d.removeDirectory(name)
}
