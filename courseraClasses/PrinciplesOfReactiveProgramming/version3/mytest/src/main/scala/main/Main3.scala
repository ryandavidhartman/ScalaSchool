package main

import rx.lang.scala.Notification.{OnCompleted, OnError, OnNext}
import rx.lang.scala.Observable

import scala.concurrent.duration._
import Util._


object Main3 extends App {


  val a1 = Observable.interval(2 second) map
    (x => {
      //printMessage("x:" + x.toString);
      "a1::" + x.toString
    }
      )
  val a2 = Observable.interval(3 second) map
    (y => {
      //printMessage("y:" + y.toString);
      "a2::" + y.toString
    }
      )

  val a3 = Observable.interval(4 second) map
    (z => {
      //printMessage("z:" + z.toString);
      "a3::" + z.toString
    }
      )

  val a4 = Observable.interval(5 second) map
    (x => {
      //printMessage("a4:" + x.toString);
      "a4::" + x.toString
    }
      )

  val m = (a1 map (x => "a1hh:" + x + ">")) merge (a2 map (x => "a2hh:" + x + ">")) merge (a3 map (x => "a3hh:" + x + ">")) merge (a4 map (x => "a4hh:" + x + ">"))

  m subscribe( {
    value =>
      printMessage("Value:" + value)
  }, {
    e =>
      printMessage("Error:" + e);
  }, {
    () =>
      printMessage("Completed"); ()
  }

    )

  readLine

}
