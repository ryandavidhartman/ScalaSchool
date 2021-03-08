package section6.commands
import section6.files.{DirEntry, Directory, File, FileSystemException}
import section6.filesystem.State

class Echo(args: List[String]) extends Command {
  override def apply(state: State): State = {
    val wd = state.wd

    def fileCreateOrUpdate(fileName: String, text: String, append:Boolean): (File, Option[File])  = {
      var maybeOld: Option[File] = None

      val file = wd.findEntry(fileName)
        .filter(_.isFile)
        .fold(File.create(wd.parentPath, fileName, text)){ f =>
          maybeOld = Some(f.asFile)
          if(append)
            f.asFile.appendContents(text)
          else
            f.asFile.setContents(text)
        }

      (file, maybeOld)
    }

    args match {
      case List(text, ">", fileName) => {
        val (newFile, maybeOld) =  fileCreateOrUpdate(fileName, text, false)
        doEchoToFile(state, newFile, maybeOld)
      }
      case List(text, ">>", fileName) => {
        val (newFile, maybeOld) = fileCreateOrUpdate(fileName, text, true)
        doEchoToFile(state, newFile, maybeOld)
      }
      case _ => state.setMessage(args.mkString(" "))
    }
  }

  def throwEchoError(name: String): Nothing =
    throw new FileSystemException(s"echo error: $name can not be created")


  def doEchoToFile(state: State, fileNewFile: File, maybeOldFile: Option[File]): State = {

    val updateState = if(maybeOldFile.isEmpty) {
      state
    } else {
      val rm = new Rm(maybeOldFile.get.name)
      rm.apply(state)
    }
    val wd = updateState.wd

    // step 1
    val pathList = wd.pathAsList

    // step 3
    val newRoot = Directory.updateStructure(updateState.root, pathList, fileNewFile, throwEchoError)

    // step 4
    val newWd = newRoot.findDescendant(pathList).getOrElse(throwEchoError(fileNewFile.name))

    State(newRoot, newWd)
  }
}
