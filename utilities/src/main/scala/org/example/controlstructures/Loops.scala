package org.example.controlstructures

import scala.annotation.tailrec

case object Loops {

  /**
   * This is a sample of how to write a control structure in Scala
   * using by-name parameters and multiple parameter groups.  This
   * sample is basically just a normal while loop
   * @param condition
   * @param codeBlock
   */
  @tailrec
  def whilst(condition: => Boolean)(codeBlock: => Unit): Unit = {
    if(condition) {
      codeBlock
      whilst(condition)(codeBlock)
    }
  }

  /**
   * Note that break and breakable aren’t keywords in Scala
   * They’re methods in scala.util.control.Breaks.
   * break method is declared as follows to throw an instance
   * of a BreakControl exception when it’s called:
   */

  private val breakException = new Exception("Break out of the loop")
  def break(): Nothing = { throw breakException }

  def breakable(codeblock: => Unit): Unit = {
    try {
      codeblock
    } catch {
      case e => if(e != breakException) throw e
    }
  }

  /**
   * a using block takes a resource that has a closing block, and a function.  The using
   * block will call the function, and close the resource after.
   */
  def using[A <: {def close() }, B](resource: A)(f: A => B): B = {
    try {
      f(resource)
    } finally {
      resource.close()
    }
  }
}
