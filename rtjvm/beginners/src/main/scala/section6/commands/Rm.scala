package section6.commands
import section6.filesystem.State
import section6.files.Directory

class Rm(name: String) extends RmEntry(name) {

  def doRemove(state: State, absolutePath: String): State = ???
}
