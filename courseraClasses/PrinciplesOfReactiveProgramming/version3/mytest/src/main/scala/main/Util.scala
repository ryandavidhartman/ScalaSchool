package main

/**
 * Created by ukanitkar on 5/18/15.
 */
object Util {

  def getThreadId(): Long = {
    Thread.currentThread().getId
  }

  def printMessage(msg: String) = {
    println(msg + ";;;ThreadId="+getThreadId())
  }
}
