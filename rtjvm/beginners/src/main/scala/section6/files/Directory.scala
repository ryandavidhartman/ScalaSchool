package section6.files

import section6.files.Directory.pathStringToList

class Directory(
  override val parentPath: String,
  override val name: String,
  val contents: List[DirEntry]) extends DirEntry(parentPath, name) {
  val isRoot: Boolean = parentPath.isEmpty


  def asDirectory: Directory = this

  def asFile: File = throw new FileSystemException("A directory cannot be converted to a file")

  val getType: String = Directory.DIRECTORY_TYPE
  val isDirectory: Boolean = true
  val isFile: Boolean = false

  def findEntry(entryName: String): Option[DirEntry] = contents.find(_.name == entryName)

  def hasEntry(name: String): Boolean = findEntry(name).isDefined

  lazy val pathAsList: List[String] = Directory.pathStringToList(path)

  def findDescendant(path: List[String]): Option[Directory] = {
    if (path.isEmpty)
      Some(this)
    else findEntry(path.head).flatMap(e => e.asDirectory.findDescendant(path.tail))
  }

  def addEntry(newEntry: DirEntry): Directory = new Directory(parentPath, name, contents :+ newEntry)

  def replaceEntry(entryName: String, newEntry: DirEntry): Directory =
    new Directory(parentPath, name, contents.filterNot(_.name == entryName) :+ newEntry)

  def removeEntry(entry: DirEntry): Directory = {
    val nameToDelete = entry.name

    if(!hasEntry(nameToDelete))
      this
    else {
      val newContents = contents.filterNot(_.name == nameToDelete)
      new Directory(parentPath, name, newContents)
    }
  }

  def removeFile(fileName: String): Directory =
    findEntry(fileName).filter(_.isFile).map(removeEntry).getOrElse(this)

  def removeDirectory(fileName: String): Directory =
    findEntry(fileName).filter(_.isDirectory).map(removeEntry).getOrElse(this)
}

object Directory {
  val SEPARATOR = "/"
  val ROOT_PATH = "/"
  val DIRECTORY_TYPE = "Directory"

  def ROOT: Directory = Directory.empty("", "")

  def empty(parentPath: String, name: String): Directory = new Directory(parentPath, name, List())

  def pathStringToList(pathString: String): List[String] = {
    // path string "/a/b/c/d" goes to path list of List("a", "b", "c", "d")
    val path1 = pathString.split(Directory.SEPARATOR).toList.map(_.trim).filterNot(_.isBlank).filterNot(_.equals("."))

    // handle ..
    def cleaner(acc: List[String], remainder: List[String]): List[String] = remainder match {
      case l if l.isEmpty => acc
      case ".." :: _ :: ls => cleaner(acc, ls)
      case l :: ls => cleaner(acc ++ List(l), ls)
    }

    cleaner(List.empty, path1.reverse).reverse
  }

  def updateStructure(
    currentDirectory: Directory,
    pathList: List[String],
    newEntry: DirEntry,
    e: String => Nothing): Directory = {
    if(pathList.isEmpty)
      currentDirectory.addEntry(newEntry)
    else {
      val nextSubDir = currentDirectory.findEntry(pathList.head)
        .getOrElse(e(newEntry.name))
        .asDirectory
      currentDirectory.replaceEntry(nextSubDir.name, updateStructure(nextSubDir, pathList.tail, newEntry, e))
    }
  }

}
