package section6.files

abstract class DirEntry(val parentPath: String, val name: String) {

  lazy val path: String = if(parentPath != Directory.SEPARATOR)
    parentPath + Directory.SEPARATOR + name
  else
    parentPath + name

  def asDirectory: Directory
  def asFile: File

  def isDirectory: Boolean
  def isFile: Boolean

  def getType: String
}
