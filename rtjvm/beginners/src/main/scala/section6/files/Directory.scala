package section6.files

class Directory(
  override val parentPath: String,
  override val name: String,
  val contents: List[DirEntry]) extends DirEntry(parentPath, name) {

  def asDirectory: Directory = this

  def getType: String = Directory.DIRECTORY

  def findEntry(entryName: String): Option[DirEntry] = contents.find(_.name == entryName)

  def hasEntry(name: String): Boolean = findEntry(name).isDefined

  lazy val pathAsList: List[String] = {
    // path string "/a/b/c/d" goes to path list of List("a", "b", "c", "d")
    path.split(Directory.SEPARATOR).toList.filter(_.isBlank)
  }

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
  val DIRECTORY = "Directory"

  def ROOT: Directory = Directory.empty("", "")

  def empty(parentPath: String, name: String): Directory = new Directory(parentPath, name, List())

}
