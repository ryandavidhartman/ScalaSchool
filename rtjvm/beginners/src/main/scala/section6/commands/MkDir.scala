package section6.commands
import section6.files.{DirEntry, Directory}
import section6.filesystem.State

class MkDir(name: String) extends CreateEntry(name) {
  override def createEntry(state: State): DirEntry = Directory.empty(state.wd.path, name)

  override def checkIllegal(name: String): Boolean = name match {
    case n if n.contains(Directory.SEPARATOR) => true
    case n if n.contains(".") => true
    case _ => false
  }
}