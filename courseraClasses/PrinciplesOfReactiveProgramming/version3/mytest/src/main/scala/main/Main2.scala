package main

import _root_.rx.lang.scala.Notification.OnCompleted
import _root_.rx.lang.scala.Notification.OnError
import _root_.rx.lang.scala.Notification.OnNext
import _root_.rx.lang.scala.Observable
import main.Util
import main.Util._
import rx.lang.scala.Notification.{OnCompleted, OnNext, OnError}
import rx.lang.scala.{Observer, Observable}
import scala.concurrent._
import scala.concurrent.duration._

/**
 * Created by ukanitkar on 5/16/15.
 */
object Main2 extends App{

  def getThreadId(): Long = {
    Thread.currentThread().getId
  }

  def printMessage(msg: String) = {
    println(msg + ";;;ThreadId="+getThreadId())
  }


  val a1 = Observable.interval(1 second) map
    (x => {
      printMessage("x:" + x.toString);
      x}
      ) map
    (y =>
    {
      printMessage("y:" + y.toString);
      y*2
    }) map
    (z => {Thread.sleep(50); printMessage("z:" + z.toString); z*2}) map
    (z1 =>
    {
      printMessage("z1:" + z1.toString);
      z1*2})

  a1 subscribe (
    {
      value =>
        printMessage("Value:"+ value)
    },
    {
      e =>
        printMessage("Error:" + e);
    },
    {
      () =>
        printMessage("Completed");()
    }

    )

  readLine


  val z1 = Observable.just(4,3,2,1)

  val sub = z1 map (x => if (x == 2) x/0 else x) subscribe (
    {
      value =>
        printMessage("Value:"+ value)

    },
    {
      e =>
        printMessage("Error:" + e);
    },
    {
      () =>
        printMessage("Completed");()}
    )




  readLine

  val mat = z1 map (x => if (x == 2) x/0 else x) materialize

  mat subscribe (
    {
      value => value match {
        case OnNext(v) => printMessage("Mat onNext:" + v)
        case OnError(e) => printMessage("Mat OnError:" + e)
        case OnCompleted => printMessage("Mat OnCompleted")
      }

    },
    {
      e =>
        printMessage("Mat Error:" + e);
    },
    {
      () =>
        printMessage("Mat Completed");()}
    )

  readLine()

  val y1 = Observable.just(1,2,3)
  y1.foreach( x =>
  {
    println(x);
    //1
  })

  readLine

  y1.foreach( x =>
  {
    println(x);
    //1
  })

  val x1 = Observable.interval(1 second)

  x1.foreach( x =>
  {
    println("T1:" +x);
    //1
  })

  readLine
  x1.foreach( x =>
  {
    println("T2:" + x);
    //1
  })
  readLine

  x1 subscribe (
    value  => {if(value < 15)println(s"from x1: $value")},
    t  => {println(s"from x1: $t")},
    () => {println("Completed x1")}
    )

  val x2 = x1.takeUntil(Observable.timer(10 second))
  x2 subscribe (
    value  => {println(s"from x2: $value")},
    t  => {println(s"from x2: $t")},
    () => {println("Completed x2")}
    )


  val x3 = x2.filter(x => x%2 == 0)
  x3 subscribe (
    value  => {println(s"from x3: $value")},
    t  => {println(s"from x3: $t")},
    () => {println("Completed x3")}
    )

  val x4 = x3.map(x => if (x > 4) throw new Exception("test exception") else x * x)
  x4 subscribe (
    value  => {println(s"from x4: $value")},
    t  => {println(s"from x4: $t")},
    () => {println("Completed x4")}
    )

  val x5 = x2.toList
  x5 subscribe (
    value  => {println(s"from x5: $value")},
    t  => {println(s"from x5: $t")},
    () => {println("Completed x5")}
    )

  val x6 = x2.toList
  x6 subscribe (
    value  => {println(s"from x6: $value")},
    t  => {println(s"from x6: $t")},
    () => {println("Completed x6")}
    )

  val x7 = x4.materialize.subscribe(
    value  => {println(s"from x7: $value")},
    t  => {println(s"from x7: $t")},
    () => {println("Completed x7")}
  )

  val x8 = x4.materialize
  x8.subscribe(
    value  =>
      value match {
        case OnNext(k) =>
          println(s"found OnNext: $k")
        case OnError(t) =>
          println(s"found OnError: $t")
        case OnCompleted =>
          println(s"from x8 completed")
      }, //println(s"from x8: $value")},
    t  => {println(s"from x8: $t")},
    () => {println("Completed x8")}
  )
  readLine


}


