package org.example.controlstructures

case object Loops {

  /**
   * This is a sample of how to write a control structure in Scala
   * using by-name parameters and multiple parameter groups.  This
   * sample is basically just a normal while loop
   * @param condition
   * @param codeBlock
   */
  def whilst(condition: => Boolean)(codeBlock: => Unit): Unit = {
    if(condition) {
      codeBlock
      whilst(condition)(codeBlock)
    }
  }

}
