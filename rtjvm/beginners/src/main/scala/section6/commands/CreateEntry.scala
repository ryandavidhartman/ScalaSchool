package section6.commands
import section6.files.{DirEntry, Directory, FileSystemException}
import section6.filesystem.State

abstract class CreateEntry(name: String) extends Command {

  override def apply(state: State): State = {
    val wd = state.wd

    if(wd.hasEntry(name))
      state.setMessage(s"$name already exists")
    else if(checkIllegal(name))
      state.setMessage(s"""Invalid command.  "$name" is an invalid name""")
    else
      doMakeDirectoryEntry(state, name)
  }

  def checkIllegal(name: String): Boolean

  def throwMkDirError(name: String): Nothing =
    throw new FileSystemException(s"Mkdir error: $name can not be created")

  def doMakeDirectoryEntry(state: State, name: String): State = {
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
    val newEntry: DirEntry = createEntry(state)

    // step 3
    val newRoot = Directory.updateStructure(state.root, pathList, newEntry, throwMkDirError)

    // step 4
    val newWd = newRoot.findDescendant(pathList).getOrElse(throwMkDirError(name))

    State(newRoot, newWd)
  }

  def createEntry(state: State): DirEntry
}
