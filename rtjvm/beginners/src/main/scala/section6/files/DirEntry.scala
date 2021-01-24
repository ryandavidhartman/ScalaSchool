package section6.files

abstract class DirEntry(val parentPath: String, val name: String) {

  def  path: String = parentPath + Directory.SEPARATOR + name
}
