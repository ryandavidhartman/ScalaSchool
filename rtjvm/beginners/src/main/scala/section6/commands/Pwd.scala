package section6.commands
import section6.filesystem.State

class Pwd extends Command {

  def apply(state: State): State = state.setMessage(state.wd.path)

}
