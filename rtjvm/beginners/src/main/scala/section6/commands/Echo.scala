package section6.commands
import section6.files.File
import section6.filesystem.State

class Echo(args: List[String]) extends Command {
  override def apply(state: State): State = {

    val wd =  state.wd

    args match {
      case List(text, ">", fileName) => {
        val maybeFile:Option[File] = wd.findEntry(fileName)
          .filter(_.isFile)
          .map(f => f.asFile.setContents(text.mkString(" ")))
        replaceFile(fileName, maybeFile, state)
      }
      case List(text, ">>", fileName) => {
        val maybeFile:Option[File] = wd.findEntry(fileName)
          .filter(_.isFile)
          .map(f => f.asFile.appendContents(text.mkString(" ")))
        replaceFile(fileName, maybeFile, state)
      }
      case _ => state.setMessage(args.mkString(" "))
    }
  }

  def replaceFile(fileName: String, maybeFile: Option[File], state: State): State = {
    if(maybeFile.isEmpty) {
      state
    } else {
      val wd = state.wd
      val file = maybeFile.get
      val newWd = wd.replaceEntry(fileName, file)
      new State(state.root, newWd, s"file $fileName created")
    }
  }
}
