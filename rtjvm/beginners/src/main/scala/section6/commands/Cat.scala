package section6.commands
import section6.filesystem.State

class Cat(name: String) extends Command {
  override def apply(state: State): State = {
    val wd = state.wd

    val dirEntry = wd.findEntry(name)
    dirEntry.fold(state.setMessage(s"$name: no such file")){de =>
      state.setMessage(de.asFile.contents)
    }
  }
}
