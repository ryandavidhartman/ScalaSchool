package org.example.utilities

case object Timer {


  /**
   *
   * @param codeBlock - Some block of code you wish to time
   * @tparam A - Return type of the code block
   * @return - A Tuple (A, Long) which is the result the
   *         evaluation of the code block, and the execution time
   *         in MS
   */
  def time[A](codeBlock: => A): (A, Double) = {
     val start = System.nanoTime()
     val result = codeBlock
     val duration = (System.nanoTime() - start) / 1000000.0
    (result, duration)
  }
}
