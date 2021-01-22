package section6.commands

import section6.filesystem.State

trait Command {

  def apply(state: State): State

}

object Command {

  def from(input: String): Command = new UnknownCommand

}
