package week02


// Question: When can I expected the deploy task to be finished?

// Here are some arbitrary tasks:

case class Task(name: String, duration: Int, requirements: List[Task])

val csSetup = Task("cs setup", 4, Nil)
val ide = Task("IDE", 3 , Nil)
val hack = Task("hack", 8, List(csSetup, ide))
val deploy = Task("deploy", 3, List(hack))


/* Standard recursion
val maxTotalDuration: (List[Task]) => Int = (tasks) =>
  tasks match
    case Nil => 0
    case head :: tail =>
      val headDuration = totalDuration(head)
      val tailDuration = maxTotalDuration(tail)
      math.max(headDuration, tailDuration)

val totalDuration: (Task) => Int = (task) =>
  task.duration + maxTotalDuration(task.requirements)
*/

def totalDuration(task: Task): Int =
  val requirementsMaxTotalDuration =
    task.requirements
      .map(totalDuration)
      .maxOption
      .getOrElse(0)

  task.duration + requirementsMaxTotalDuration



object TaskManagement extends App {

  println(totalDuration(deploy))
}
