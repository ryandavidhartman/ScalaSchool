package section6.commands
import section6.files.{DirEntry, Directory}
import section6.filesystem.State

import scala.annotation.tailrec

class Cd(dir: String) extends Command {

  def findDirectory(root: Directory, absolutePath: String): Option[Directory] = {
    val pathAsList: List[String] = Directory.pathStringToList(absolutePath)

    def findDirectoryHelper(currentDirectory: Directory, path: List[String]):Option[Directory] = {
      if(path.isEmpty)
        Some(currentDirectory)
      else if(path.tail.isEmpty)
        currentDirectory.findEntry(path.head).map(_.asDirectory)
      else
        currentDirectory.findEntry(path.head).map(_.asDirectory).flatMap(nd => findDirectoryHelper(nd, path.tail))
    }

    findDirectoryHelper(root, pathAsList)
  }

  override def apply(state: State): State = {
    /*
    Support for:
     1) an absolute path  e.g. /a/b/c
     2) a path relative to the current working directory  e.g  b (where b is a subdirectory in the current wd)

     Step 1: Find the root
     Step 2: Find the absolute path of the directory that I want cd into
     Step 3: Find the target directory
     Step 4: Change the state
     */

    val root = state.root
    val wd = state.wd

    val absolutePath = if(dir.startsWith(Directory.SEPARATOR))
      dir
    else if(wd.isRoot)
      wd.path + dir
    else
      wd.path + Directory.SEPARATOR + dir
      
    val destinationDir = findDirectory(root, absolutePath)
    if(destinationDir.isEmpty)
      state.setMessage(s"$dir no such directory")
    else
      State(root, destinationDir.get)
  }
}
