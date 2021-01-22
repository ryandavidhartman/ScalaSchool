package section6.commands
import section6.filesystem.State

class UnknownCommand extends  Command {

  override def apply(state: State): State = state.setMessage("Command not found!")

}
