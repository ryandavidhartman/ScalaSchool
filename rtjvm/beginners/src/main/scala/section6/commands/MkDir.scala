package section6.commands
import section6.files.Directory
import section6.filesystem.State

class MkDir(name: String) extends Command {

  override def apply(state: State): State = {
    val wd = state.wd

    if(wd.hasEntry(name))
      state.setMessage(s"$name already exists")
    else if(checkIllegal(name))
      state.setMessage(s"""Invalid command.  "$name" is an invalid name""")
    else
      doMkDir(state, name)
  }

  def checkIllegal(name: String): Boolean = name match {
    case n if n.contains(Directory.SEPARATOR) => true
    case n if n.contains(".") => true
    case _ => false
  }

  def doMkDir(state: State, name: String): State = {

    state
  }
}
