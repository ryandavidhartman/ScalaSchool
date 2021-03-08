package section6.filesystem

import section6.commands.Command
import section6.files.Directory

object FileSystem extends App {

  val root = Directory.ROOT
  val inputStream = io.Source.stdin.getLines()

  val startingState = State(root, root)

  inputStream.foldLeft(startingState.show)( (currentState, line) => Command.from(line).apply(currentState).show)
}
