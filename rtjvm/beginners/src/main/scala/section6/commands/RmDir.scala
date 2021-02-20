package section6.commands
import section6.files.Directory
import section6.filesystem.State

class RmDir(name: String) extends RmEntry(name) {
  override def doRemove(currentDirectory: Directory, path: List[String]): Directory = currentDirectory
}
