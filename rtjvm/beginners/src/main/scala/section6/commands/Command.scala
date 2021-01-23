package section6.commands

import section6.filesystem.State

trait Command {

  def apply(state: State): State

}

object Command {

  val MKDIR = "mkdir"

  def emptyCommand: Command = (state: State) => state

  def incompleteCommand(name: String): Command = _.setMessage(s"$name needs more parameters")

  def from(input: String): Command = {
    val tokens = input.split(" ").toList
    tokens match {
      case List("") => emptyCommand
      case MKDIR +: tokens => {
        if (tokens.isEmpty)
          incompleteCommand(MKDIR)
        else
          new MkDir(tokens(1))
      }
      case _ => new UnknownCommand
    }
  }


}