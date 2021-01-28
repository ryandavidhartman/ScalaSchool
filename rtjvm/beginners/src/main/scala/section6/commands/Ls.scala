package section6.commands

import section6.files.DirEntry
import section6.filesystem.State

class Ls extends Command {

  def makeContentString(contents: List[DirEntry]): String = {
    contents.map(de => s"${de.name} [${de.getType}]").mkString("\n")
  }

  override def apply(state: State): State = state.setMessage(makeContentString(state.wd.contents))
}
