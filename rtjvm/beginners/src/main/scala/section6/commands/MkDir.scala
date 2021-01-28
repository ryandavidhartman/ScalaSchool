package section6.commands
import section6.files.{DirEntry, Directory}
import section6.filesystem.State

class MkDir(name: String) extends Command {

  override def apply(state: State): State = {
    val wd = state.wd

    if(wd.hasEntry(name))
      state.setMessage(s"$name already exists")
    else if(checkIllegal(name))
      state.setMessage(s"""Invalid command.  "$name" is an invalid name""")
    else
      doMkDir(state, name)
  }

  def checkIllegal(name: String): Boolean = name match {
    case n if n.contains(Directory.SEPARATOR) => true
    case n if n.contains(".") => true
    case _ => false
  }

  def throwMkDirError(name: String): Nothing =
    throw new RuntimeException(s"Mkdir error: $name can not be created")

  def doMkDir(state: State, name: String): State = {

    def updateStructure(currentDirectory: Directory, pathList: List[String], newEntry: DirEntry): Directory = {
      if(pathList.isEmpty)
        currentDirectory.addEntry(newEntry)
      else {
        val nextSubDir = currentDirectory.findEntry(pathList.head)
          .getOrElse(throwMkDirError(newEntry.name))
          .asDirectory
        currentDirectory.replaceEntry(nextSubDir.name, updateStructure(nextSubDir, pathList.tail, newEntry))
      }
    }

    /* Steps:
    1. get all the directories in the full path (i.e. all parent directories from root to wd)
    2. create a new directory entry in the wd
    3. update the whole directory structure starting from the root recall the directory structure is immutable
    4. find the new working directory instance given the wd's full path, in the NEW directory structure.
    */

    val wd = state.wd

    // step 1
    val pathList = wd.pathAsList

    // step 2
    val newDir = Directory.empty(wd.path, name)

    // step 3
    val newRoot = updateStructure(state.root, pathList, newDir)

    // step 4
    val newWd = newRoot.findDescendant(pathList).getOrElse(throwMkDirError(name))

    State(newRoot, newWd)
  }
}
