package com.example.v1

class BankAccount {

  private var balance:Int = 0

  def getBalance:Int = balance

  def deposit(amount: Int): Unit =
    if (amount > 0) balance += amount

  def withdraw(amount: Int): Int = {
    println(s"Current balance is: $balance. Attempting to withdraw: $amount.")
    Thread.sleep(5)
    val b = balance
    if(0 < amount && amount <= b) {
      val newBalance = b - amount
      balance = newBalance
      println(s"Successfully withdrawn: $amount")
      newBalance
    } else {
      println(s"Error withdrawing: $amount")
      throw new Exception("insufficient funds")
    }
  }
}
