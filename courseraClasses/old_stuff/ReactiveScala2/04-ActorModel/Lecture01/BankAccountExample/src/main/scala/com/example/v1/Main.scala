package com.example.v1

import scala.util.Try
import scala.util.control.NonFatal

object Main extends App {

  println("Starting")

  val bankAccount = new BankAccount
  bankAccount.deposit(amount = 80)

  val thread1 = new Thread {
    override def run(): Unit = {
      try {
        val amount = 50
        val nb = bankAccount.withdraw(amount = amount)
        println(s"Thread1 Success, withdrew: $amount New Balance: $nb")
      } catch {
        case NonFatal(e) => println(s"Thread1 error: ${e.getMessage}, , NewBalance: ${bankAccount.getBalance}")
      }
    }
  }

  val thread2 = new Thread {
    override def run(): Unit = {
      try {
        val amount = 40
        val nb = bankAccount.withdraw(amount = amount)
        println(s"Thread2 Success, withdrew: $amount New Balance: $nb")
      } catch {
        case NonFatal(e) => println(s"Thread2 error: ${e.getMessage}, NewBalance: ${bankAccount.getBalance}")
      }
    }
  }

  thread1.start()
  thread2.start()





}
