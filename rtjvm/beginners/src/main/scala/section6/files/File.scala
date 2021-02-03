package section6.files

import section6.files.File.FILE_TYPE

class File(
  override val parentPath: String,
  override val name: String,
  val contents: String) extends DirEntry(parentPath, name) {

  def asDirectory: Directory = throw new FileSystemException("A file cannot be converted to a directory")
  def asFile: File = this
  lazy val getType: String = FILE_TYPE
  lazy val isDirectory: Boolean = false
  lazy val isFile: Boolean = true
}

object File {

  val FILE_TYPE = "File"

  def empty(parentPath: String, name: String): File = create(parentPath, name, "")
  def create(parentPath: String, name: String, contents: String): File = new File(parentPath, name, contents)
}