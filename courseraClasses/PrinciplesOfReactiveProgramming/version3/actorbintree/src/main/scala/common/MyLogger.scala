package common

/**
 * Created by umesh on 5/23/15.
 */
object MyLogger {

  val on = false
  def println1(str:String): Unit = {
    if(on) println(str)
  }
}
