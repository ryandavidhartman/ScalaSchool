package section6.commands
import section6.files.{DirEntry, File}
import section6.filesystem.State

class Echo(args: List[String]) extends Command {
  override def apply(state: State): State = {
    val wd = state.wd

    def fileCreateOrUpdate(fileName: String, text: String, append:Boolean): File  = {
      wd.findEntry(fileName)
        .filter(_.isFile)
        .fold(File.create(wd.parentPath, fileName, text)){ f =>
          if(append)
            f.asFile.appendContents(text)
          else
            f.asFile.setContents(text)
        }
    }

    args match {
      case List(text, ">", fileName) => updateStructure(state, fileCreateOrUpdate(fileName, text, false))
      case List(text, ">>", fileName) => updateStructure(state, fileCreateOrUpdate(fileName, text, true))
      case _ => state.setMessage(args.mkString(" "))
    }
  }


  def updateStructure(state: State, dirEntry: DirEntry): State = ???
}
