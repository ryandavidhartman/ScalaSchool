package section6.files

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.{convertToAnyShouldWrapper, equal}

class DirectorySpecs extends AnyFlatSpec {

  "pathStringToList()" should "work with relative paths" in {
    val test1 = "/a/b/c/d"
    val answer1 = List("a", "b", "c", "d")
    assert(Directory.pathStringToList(test1) == answer1)

    val test2 = "/a/./b/c/d"
    val answer2 = List("a", "b", "c", "d")
    assert(Directory.pathStringToList(test2) == answer2)

    val test3 = "/a/../b/c/d"
    val answer3 = List("b", "c", "d")
    assert(Directory.pathStringToList(test3) == answer3)
  }

}
