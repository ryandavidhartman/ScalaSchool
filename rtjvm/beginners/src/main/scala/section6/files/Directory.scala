package section6.files

class Directory(
  override val parentPath: String,
  override val name: String,
  val contents: List[DirEntry]) extends DirEntry(parentPath, name) {

  def hasEntry(name: String): Boolean = ???

  lazy val pathAsList: List[String] = {
    // path string "/a/b/c/d" goes to path list of List("a", "b", "c", "d")
    path.split(Directory.SEPARATOR).toList.tail
  }

  def findDescendant(path: List[String]): Directory = ???

  def addEntry(newEntry: DirEntry): Directory = ???

}

object Directory {
  val SEPARATOR = "/"
  val ROOT_PATH = "/"

  def ROOT: Directory = Directory.empty("", "")

  def empty(parentPath: String, name: String): Directory = new Directory(parentPath, name, List())

}
