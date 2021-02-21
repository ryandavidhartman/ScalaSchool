package section6.commands
import section6.filesystem.State

class Echo(args: List[String]) extends Command {
  override def apply(state: State): State = {
    // fake implementation
    state.setMessage(args.mkString(" "))
  }
}
