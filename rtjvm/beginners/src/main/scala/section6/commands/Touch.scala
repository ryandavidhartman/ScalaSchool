package section6.commands
import section6.files.{DirEntry, Directory, File}
import section6.filesystem.State


class Touch(name: String) extends CreateEntry(name) {
  override def createEntry(state: State): DirEntry = File.empty(state.wd.parentPath, name)

  override def checkIllegal(name: String): Boolean = File.checkIfNameIsIllegal(name)
}