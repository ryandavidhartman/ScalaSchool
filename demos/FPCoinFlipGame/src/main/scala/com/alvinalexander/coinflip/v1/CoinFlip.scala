package com.alvinalexander.coinflip.v1

import CoinFlipUtils._
import scala.annotation.tailrec

case class GameState(numFlips: Int, numCorrect: Int)

object CoinFlip extends App {

    mainLoop(GameState(0, 0))

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
