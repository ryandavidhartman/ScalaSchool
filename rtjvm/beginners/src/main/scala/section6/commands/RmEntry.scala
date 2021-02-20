package section6.commands
import section6.files.{Directory, FileSystemException}
import section6.filesystem.State

abstract class RmEntry(name: String) extends Command {
  def apply(state: State): State = {
    // get working dir
    val wd = state.wd

    // get absolute path of entry we are deleting
    val absolutePath = if(name.startsWith(Directory.SEPARATOR))
      name
    else if(wd.isRoot)
      wd.path + name
    else
      s"${wd.path}${Directory.SEPARATOR}$name"

    val newRoot = doRemove(state.root, Directory.pathStringToList(absolutePath))

    if(newRoot == state.root)
      state.setMessage(s"$absolutePath: no such file or directory")
    else {
      val newWd =  newRoot.findDescendant(Directory.pathStringToList(state.wd.path))
        .getOrElse(throw new FileSystemException(s"Can not set WD  tp ${state.wd.path}"))
      State(newRoot, newWd)
    }

  }

  def doRemove(currentDirectory: Directory, path: List[String]): Directory

}
