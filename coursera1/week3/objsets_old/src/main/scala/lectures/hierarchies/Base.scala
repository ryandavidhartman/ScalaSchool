package lectures.hierarchies

/**
  * It is also possible to redefine an existing, non-abstract definition in a subclass
  * by using override
  */

abstract class Base {
  def foo =1
  def bar: Int
}

class Sub extends Base {
  override def foo = 2
  def bar = 3
}
