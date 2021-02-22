package section6.commands

import section6.filesystem.State

trait Command {

  def apply(state: State): State

}

object Command {

  val CD = "cd"
  val ECHO = "echo"
  val LS = "ls"
  val MKDIR = "mkdir"
  val PWD = "pwd"
  val RM = "rm"
  val RMDIR = "rmdir"
  val TOUCH = "touch"

  def emptyCommand: Command = (state: State) => state

  def incompleteCommand(name: String): Command = _.setMessage(s"$name needs more parameters")

  def from(input: String): Command = {
    input.split(" ").toList match {
      case List("") => emptyCommand
      case CD +: tokens => {
        if (tokens.isEmpty)
          incompleteCommand(CD)
        else
          new Cd(tokens.head)
      }
      case ECHO +: tokens =>
        new Echo(tokens)
      case MKDIR +: tokens => {
        if (tokens.isEmpty)
          incompleteCommand(MKDIR)
        else
          new MkDir(tokens.head)
      }
      case RM +: tokens => {
        if (tokens.isEmpty)
          incompleteCommand(RM)
        else
          new Rm(tokens.head)
      }
      case RMDIR +: tokens => {
        if (tokens.isEmpty)
          incompleteCommand(RMDIR)
        else
          new RmDir(tokens.head)
      }
      case TOUCH +: tokens => {
        if (tokens.isEmpty)
          incompleteCommand(TOUCH)
        else
          new Touch(tokens.head)
      }
      case LS +: _ => new Ls
      case PWD +: _ => new Pwd
      case _ => new UnknownCommand
    }
  }


}