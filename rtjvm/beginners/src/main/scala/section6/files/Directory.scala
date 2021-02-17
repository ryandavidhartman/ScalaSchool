package section6.files

class Directory(
  override val parentPath: String,
  override val name: String,
  val contents: List[DirEntry]) extends DirEntry(parentPath, name) {
  val isRoot: Boolean = parentPath.isEmpty


  def asDirectory: Directory = this
  def asFile: File = throw new FileSystemException("A directory cannot be converted to a file")
  lazy val getType: String = Directory.DIRECTORY_TYPE
  lazy val isDirectory: Boolean = true
  lazy val isFile: Boolean = false

  def findEntry(entryName: String): Option[DirEntry] = contents.find(_.name == entryName)

  def hasEntry(name: String): Boolean = findEntry(name).isDefined

  lazy val pathAsList: List[String] = Directory.pathStringToList(path)

  def findDescendant(path: List[String]): Option[Directory] = {
    if(path.isEmpty)
      Some(this)
    else findEntry(path.head).flatMap(e => e.asDirectory.findDescendant(path.tail))
  }

  def addEntry(newEntry: DirEntry): Directory = new Directory(parentPath, name, contents :+ newEntry)

  def replaceEntry(entryName: String, newEntry: DirEntry): Directory =
    new Directory(parentPath, name, contents.filterNot(_.name == entryName) :+ newEntry)
}

object Directory {
  val SEPARATOR = "/"
  val ROOT_PATH = "/"
  val DIRECTORY_TYPE = "Directory"

  def ROOT: Directory = Directory.empty("", "")

  def empty(parentPath: String, name: String): Directory = new Directory(parentPath, name, List())

  def pathStringToList(pathString: String): List[String] = {
    // path string "/a/b/c/d" goes to path list of List("a", "b", "c", "d")
    pathString.split(Directory.SEPARATOR).toList.map(_.trim).filterNot(_.isBlank).filterNot(_.equals("."))
  }

}
