package section6.commands
import section6.filesystem.State
import section6.files.Directory

class Rm(name: String) extends RmEntry(name) {
  override  val failMessage: String => String = (f:String) => s"No such file: $f"

  override val remover: Directory => String => Directory = (d: Directory) => (name: String) => d.removeFile(name)
}
