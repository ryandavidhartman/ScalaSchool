package section6.commands
import section6.files.Directory
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

    doRemove(state, absolutePath)
  }

  def doRemove(state: State, absolutePath: String): State

}
