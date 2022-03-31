package simulations

import math.random
import scala.util.Random

class EpidemySimulator extends Simulator {

  def randomBelow(i: Int) = (random * i).toInt

  protected[simulations] object SimConfig {
    val population: Int = 300
    val roomRows: Int = 8
    val roomColumns: Int = 8
    val maxDaysToMove: Int = 5

    val incubationTime = 6
    val dieTime = 14
    val immuneTime = 16
    val healTime = 18

    val prevalenceRate = 0.01
    val transRate = 0.4
    val dieRate = 0.25

    val airTrafficProbability = 0
    val VIPmode = false
    val reducedMobilityMode = false
  }

  import SimConfig._

  val persons: List[Person] = initPersons()

  def initPersons(): List[Person] = {
    var result = List[Person]()

    var personId = 0
    var cell = new Cell(0, 0)

    for (peopleCount <- 0 to population - 1) {

      var person = new Person(peopleCount)

      person.row = cell.row
      person.col = cell.col

      result = person :: result

      cell = cell.next()
    }

    if (VIPmode)
      vacination(result)

    initInfection(result)

    return result
  }

  private[EpidemySimulator] def vacination(list: List[Person]) = {
    var vipAmount = (population * 0.05).toInt

    for (a <- 1 to vipAmount) {
      var nonInvulnerable = list.filterNot(p => p.invulnerability)
      nonInvulnerable(randomBelow(nonInvulnerable.length)).invulnerability = true
    }
  }

  private[EpidemySimulator] def initInfection(list: List[Person]) = {

    var a = 0
    var prevalenceAmount = (population * prevalenceRate).toInt

    for (a <- 1 to prevalenceAmount) {
      list(randomBelow(population)).infect()
    }
  }

  class Person(val id: Int) {

    var infected = false
    var sick = false
    var immune = false
    var dead = false
    var invulnerability = false

    var row = 0
    var col = 0

    def start() {
      moveAction(getMovePeriod)
    }

    def moveAction(daysToMove: Int) {

      afterDelay(daysToMove) {

        if (!dead) {
          if (random < airTrafficProbability) {
            takeAirPlane
          } else {
            var availibleRooms = getAvailibleRooms()
            if (availibleRooms.length > 0) {
              val randomRoomNumber = randomBelow(availibleRooms.length)
              var room = availibleRooms(randomRoomNumber)
              row = room.row
              col = room.col
            }
          }

          tryToInfect()
        }
        moveAction(getMovePeriod)
      }
    }

    def takeAirPlane {
      row = randomBelow(roomRows)
      col = randomBelow(roomColumns)
    }

    def tryToInfect() {
      if (infected || immune || invulnerability) return

      if (hasInfected(row, col) && random <= transRate) {
        infect()
      }
    }

    def infect() = {
      infected = true

      afterDelay(incubationTime) {
        sick = true
      }

      afterDelay(dieTime) {
        dead = random < dieRate
      }

      afterDelay(immuneTime) {
        if (!dead) {
          immune = true
          sick = false
        }
      }

      afterDelay(healTime) {
        if (!dead) {
          infected = false
          immune = false
        }
      }
    }

    def getMovePeriod(): Int = {
      var movePeriod = randomBelow(maxDaysToMove) + 1
      if (reducedMobilityMode){
        movePeriod= movePeriod*2
        if (sick)
          movePeriod= movePeriod*2
      }
      
      movePeriod
    }

    private[Person] def getAvailibleRooms(): List[Cell] = {

      var rooms = List[Cell]()
      var currentRoom = new Cell(row, col)

      var variant = new Cell(currentRoom.row, currentRoom.nextColumn)

      if (!hasVisiblyInfectious(variant.row, variant.col))
        rooms = variant :: rooms

      variant = new Cell(currentRoom.row, currentRoom.prevColumn)

      if (!hasVisiblyInfectious(variant.row, variant.col))
        rooms = variant :: rooms

      variant = new Cell(currentRoom.nextRow, currentRoom.col)

      if (!hasVisiblyInfectious(variant.row, variant.col))
        rooms = variant :: rooms

      variant = new Cell(currentRoom.prevRow, currentRoom.col)

      if (!hasVisiblyInfectious(variant.row, variant.col))
        rooms = variant :: rooms

      return rooms
    }

    def hasVisiblyInfectious(row: Int, col: Int) = {
      persons.exists({ p => p.row == row && p.col == col && (p.sick || p.dead) })
    }

    def hasInfected(row: Int, col: Int) = {
      persons.exists({ p => p.row == row && p.col == col && p.infected })
    }

    start()
  }

  class Cell(var row: Int, var col: Int) {

    def next(): Cell = {
      col = nextColumn

      if (col == 0) {
        row = nextRow
      }

      return new Cell(row, col)
    }

    def nextRow() = { nextIndex(row, roomRows) }
    def nextColumn() = { nextIndex(col, roomColumns) }
    def prevRow() = { prevIndex(row, roomRows) }
    def prevColumn() = { prevIndex(col, roomColumns) }

    def nextIndex(current: Int, max: Int) = {
      if (current != max - 1) current + 1 else 0
    }

    def prevIndex(current: Int, max: Int) = {
      if (current != 0) current - 1 else max - 1
    }
  }
}
