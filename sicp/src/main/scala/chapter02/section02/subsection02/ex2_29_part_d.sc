import ScalaScheme.Primitives._
import ScalaScheme.Primitives.SchemeData

/* Exercise 2.29

Part D.  Suppose we change the representation of mobiles so that the constructors are

(define (make-mobile left right)
(cons left right))
(define (make-branch length structure)
(cons length structure))

How much do you need to change your programs to convert to the new representation?
*/

// Part A
// Write the corresponding selectors left-branch and right-branch, which return the branches of a mobile,
// and branch-length and branch-structure, which return the components of a branch.

object PartA {
  import ScalaScheme.Primitives._

  type Pair = (SchemeData, SchemeData)
  def cons(l: SD, r: SD):Pair = (l, r)
  def car(d: SD): SD = d.asInstanceOf[Pair]._1
  def cdr(d: SD): SD = d.asInstanceOf[Pair]._2

  def make_mobile(leftBranch: SchemeData, rightBranch: SchemeData): Pair = cons(leftBranch, rightBranch)
  def make_branch(length: Double, structure: SchemeData): Pair = cons(length, structure)

  def left_branch(mobile: SchemeData): SchemeData = car(mobile)

  //CHANGE 1 drop the CAR
  def right_branch(mobile: SchemeData): SchemeData = cdr(mobile)

  def branch_length(branch: SchemeData): SchemeData = car(branch)

  //CHANGE 2 drop the CAR
  def branch_structure(branch: SchemeData): SchemeData = cdr(branch)
}

// Part B
// Using your selectors, define a procedure total-weight that returns the total weight of a mobile.

object PartB {

  import PartA._

  def isMobile(data: SchemeData): Boolean = data match {
    //CHANGE 3 look for a Pair not a list
    case m: Pair => true
    case _ => false
  }

  def total_weight(structure: SchemeData): Double = {
    if(isMobile(structure))
      total_weight(branch_structure(left_branch(structure))) + total_weight(branch_structure(right_branch(structure)))
    else
      structure.toString.toDouble
  }

}

/* Part C

A mobile is said to be balanced if the torque applied by its top-left branch is equal to that applied by its
top-right branch (that is, if the length of the left rod multiplied by the weight hanging from that rod is equal to
the corresponding product for the right side) and if each of the submobiles hanging off its branches is balanced.
Design a predicate that tests whether a binary mobile is balanced.
*/
object PartC {
  import PartA._
  import PartB._

  def torque(branch: SchemeData): Double = {
    val length = branch_length(branch).toString.toDouble
    val weight = total_weight(branch_structure(branch))
    length * weight
  }
  def isTorqueBalanced(left: SchemeData, right: SchemeData): Boolean = torque(left) == torque(right)

  def isBalanced(data: SchemeData): Boolean = {
    if(isMobile(data)) {
      val leftBranch = left_branch(data)
      val rightBranch = right_branch(data)
      val topBalanced = isTorqueBalanced(leftBranch, rightBranch)
      lazy val leftBalanced = isBalanced(branch_structure(leftBranch))
      lazy val rightBalanced = isBalanced(branch_structure(rightBranch))
      topBalanced && leftBalanced && rightBalanced
    } else
      true
  }
}



// Part B Tests
val branch1 = PartA.make_branch(length = 1.0, structure = 3.0)
val mobile1 = PartA.make_mobile(branch1, branch1)
assert(PartB.total_weight(mobile1) == 6.0)

val branch2 =  PartA.make_branch(length = 1.0, mobile1)
val mobile2 =  PartA.make_mobile(branch1,branch2)
assert(PartB.total_weight(mobile2) == 9.0)

// Part C Tests
assert(PartC.torque(branch1) == 3.0)
assert(PartC.torque(branch2) == 6.0)
assert(!PartC.isTorqueBalanced(branch1, branch2))
assert(PartC.isBalanced(mobile1))
assert(!PartC.isBalanced(mobile2))

val mobile3 =  PartA.make_mobile(
  leftBranch  =  PartA.make_branch(length = 9.0, mobile1),
  rightBranch =  PartA.make_branch(length = 6.0, mobile2)
)

PartB.total_weight(mobile3)
PartC.torque( PartA.make_branch(length = 9.0, mobile1))
PartC.torque( PartA.make_branch(length = 6.0, mobile2))

assert(!PartC.isBalanced(mobile3))
