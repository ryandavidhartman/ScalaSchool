package simulations

import org.scalatest.FunSuite
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class EpidemySuite extends FunSuite {

  test("prevalence rate") {
    val prevalenceRate = 0.01

    val es = new EpidemySimulator
    val numInfected = es.persons.count(_.infected)
    assert(numInfected == es.SimConfig.population * prevalenceRate,
      "prevalence rate should be 0.01")
  }

  test("dead person stays dead") {
    val es = new EpidemySimulator

    val chosenOne = es.persons.head
    chosenOne.infected = true
    chosenOne.sick = true
    chosenOne.dead = true
    chosenOne.immune = false

    val (row, col) = (chosenOne.row, chosenOne.col)

    val testDays = 100

    while (!es.agenda.isEmpty && es.agenda.head.time < testDays) {
      es.next

      assert(chosenOne.dead == true, "Dead person should keep dead state")
      assert(chosenOne.infected == true, "Dead person keeps infected")
      assert(chosenOne.immune == false, "Dead person cannot become immune")
      assert(chosenOne.sick == true, "Dead person keeps sick")
      assert(chosenOne.col == col && chosenOne.row == row, "Dead person cannot move")
    }
  }

  test("life cycle") {
    val es = new EpidemySimulator

    val incubationTime = 6
    val dieTime = 14
    val immuneTime = 16
    val healTime = 18

    val prevalenceRate = 0.01
    val transRate = 0.4
    val dieRate = 0.25

    val infectedPerson = (es.persons.find { _.infected }).get

    //before incubation time
    while (es.agenda.head.time < incubationTime) {
      assert(infectedPerson.infected == true, "Infected person keeps infected in 6 days")
      assert(infectedPerson.sick == false, "Infected person does not get sick in 6 days")
      assert(infectedPerson.immune == false, "Infected person cannot become immune in 6 days")
      assert(infectedPerson.dead == false, "Infected person does not die in 6 days")
      es.next
    }

    //incubation time has passed, there should be an event for getting sick
    assert(es.agenda.head.time == incubationTime, "You should set a 'sick' event after incubation time")
    while (es.agenda.head.time == incubationTime) es.next
    assert(infectedPerson.sick == true, "Infected person should become sick after 6 days")

    //wait for dieTime
    while (es.agenda.head.time < dieTime) {
      assert(infectedPerson.infected == true, "Sick person keeps infected")
      assert(infectedPerson.sick == true, "Sick person keeps sick before turning immune")
      assert(infectedPerson.immune == false, "Sick person is not immune")
      assert(infectedPerson.dead == false, "Sick person does not die before 14 infected days")
      es.next
    }

    assert(es.agenda.head.time == dieTime, "You should set a 'die' event (decides with a probability 25% whether the person dies) after 14 days")
    while (es.agenda.head.time == dieTime) es.next
  }

  test("transmissibility rate") {
    var infectedTimes = 0
    for (i <- 0 to 100) {
      val es = new EpidemySimulator
      val healthyPerson = (es.persons find { p => !p.infected }).get
      es.persons.filter(p => p != healthyPerson) foreach { _.infected = true }

      while (es.agenda.head.time < 6) es.next

      infectedTimes = infectedTimes + (if (healthyPerson.infected) 1 else 0)
    }
    assert(infectedTimes > 0, "A person should get infected according to the transmissibility rate when he moves into a room with an infectious person")
  }

  test("room has infected") {
    val es = new EpidemySimulator

    val chosenOne = es.persons.head
    val (row, col) = (chosenOne.row, chosenOne.col)

    val allPersons = es.persons.filter({ p => p.row == row && p.col == col })

    desinfectPersons(allPersons)

    assert(chosenOne.hasVisiblyInfectious(row, col) == false, "A room should not have infected persons")

    allPersons(0).infected = true
    allPersons(1).immune = true
    assert(chosenOne.hasVisiblyInfectious(row, col) == false, "Infected and immune persons are not visibly infectious")

    allPersons(0).sick = true
    assert(chosenOne.hasVisiblyInfectious(row, col) == true, "Sick persons are visibly infectious")

    allPersons(0).dead = true
    assert(chosenOne.hasVisiblyInfectious(row, col) == true, "Dead persons are visibly infectious")

    def desinfectPersons(persons: List[es.Person]) {
      persons foreach (person => {
        person.infected = false
        person.sick = false
        person.dead = false
        person.immune = false
      })
    }
  }

  test("next/prev room") {
    val es = new EpidemySimulator

    var cell = new es.Cell(4, 5)
    assert(cell.nextColumn == 6, "1 Next column index  = 6")
    assert(cell.prevColumn == 4, " 1Previous column index  = 4")
    assert(cell.nextRow == 5, "1 Next row index  = 5")
    assert(cell.prevRow == 3, "1 Previous row index  = 3")

    cell = new es.Cell(5, 0)
    assert(cell.nextColumn == 1, "2 Next column index  = 1")
    assert(cell.prevColumn == 7, "2 Previous column index  = 7")
    assert(cell.nextRow == 6, "2 Next row index  = 6")
    assert(cell.prevRow == 4, "2 Previous row index  = 4")

    cell = new es.Cell(0, 5)
    assert(cell.nextColumn == 6, "3 Next column index  = 6")
    assert(cell.prevColumn == 4, "3 Previous column index  = 4")
    assert(cell.nextRow == 1, "3 Next row index  = 1")
    assert(cell.prevRow == 7, "3 Previous row index  = 7")

    cell = new es.Cell(5, 7)
    assert(cell.nextColumn == 0, "4 Next column index  = 0")
    assert(cell.prevColumn == 6, "4 Previous column index  = 6")
    assert(cell.nextRow == 6, "4 Next row index  = 6")
    assert(cell.prevRow == 4, "4 Previous row index  = 4")

    cell = new es.Cell(7, 5)
    assert(cell.nextColumn == 6, "5 Next column index  = 6")
    assert(cell.prevColumn == 4, "5 Previous column index  = 4")
    assert(cell.nextRow == 0, "5 Next row index  = 0")
    assert(cell.prevRow == 6, "5 Previous row index  = 6")
  }
}