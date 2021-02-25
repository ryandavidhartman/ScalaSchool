package section6.commands
import section6.filesystem.State

class Cat(name: String) extends Command {
  override def apply(state: State): State = ???
}
