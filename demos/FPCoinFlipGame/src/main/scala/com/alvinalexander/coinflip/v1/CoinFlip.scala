package com.alvinalexander.coinflip.v1

import CoinFlipUtils._
import scala.annotation.tailrec

/**
 * Instead of mutating game state (e.g. gameState.numFlips += 1)
 * we simply make a new instance of the gate.
 *
 * val newState = oldState(copy = oldSate.numFlips +1)
 */
case class GameState(numFlips: Int, numCorrect: Int)

object CoinFlip extends App {

    mainLoop(GameState(0, 0))


  /**
   * Here we "Loop" with recursion instead of something like:
   * var input = ""
   * while( input != "q") {
   *   do stuff
   *   if(input == "q")
   *     quit
   * }
   */
  @tailrec
    def mainLoop(gameState: GameState): Unit = {

        showPrompt()
        val userInput = getUserInput()

        // handle the result
        userInput match {
            case "H" | "T" =>
                val coinTossResult = tossCoin()
                if (userInput == coinTossResult) {
                    val newGameState = gameState.copy(numFlips = gameState.numFlips+1, numCorrect = gameState.numCorrect+1)
                    printGameState(printableFlipResult(coinTossResult), newGameState)
                    mainLoop(newGameState)
                } else {
                    val newGameState = gameState.copy(numFlips = gameState.numFlips+1)
                    printGameState(printableFlipResult(coinTossResult), newGameState)
                    mainLoop(newGameState)
                }
            case "Q"   =>
                printGameOver()
                printGameState(gameState)
                // return out of the recursion here
            case _ =>
                printUsage()
                mainLoop(gameState)
        }
    }

}
