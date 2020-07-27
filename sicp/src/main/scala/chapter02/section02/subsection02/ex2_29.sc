import ScalaScheme.Primitives.SchemeData

/* Exercise 2.29

A binary mobile consists of two branches, a left branch and a right branch. Each branch is a rod of a certain length,
from which hangs either a weight or another binary mobile. We can represent a binary mobile using compound data by
constructing it from two branches (for example, using list):

  (define (make-mobile left right)
  (list left right))

A branch is constructed from a length (which must be a number) together with a structure, which may be either a number
(representing a simple weight) or another mobile:

  (define (make-branch length structure)
  (list length structure))

Part A.  Write the corresponding selectors left-branch and right-branch, which return the branches of a mobile, and
branch-length and branch-structure, which return the components of a branch.

Part B.  Using your selectors, define a procedure total-weight that returns the total weight of a mobile.

Part C.  A mobile is said to be balanced if the torque applied by its top-left branch is equal to that applied by its
top-right branch (that is, if the length of the left rod multiplied by the weight hanging from that rod is equal to the
corresponding product for the right side) and if each of the submobiles hanging off its branches is balanced. Design a
predicate that tests whether a binary mobile is balanced.

Part D.  Suppose we change the representation of mobiles so that the constructors are

(define (make-mobile left right)
(cons left right))
(define (make-branch length structure)
(cons length structure))

How much do you need to change your programs to convert to the new representation?
*/

def make_mobile(leftBranch: SchemeData, rightBranch: SchemeData) = List(leftBranch, rightBranch)
def make_branch(length: Double, structure: SchemeData) = List(length, structure)


// Part A
// Write the corresponding selectors left-branch and right-branch, which return the branches of a mobile,
// and branch-length and branch-structure, which return the components of a branch.

object PartA {
  import ScalaScheme.Primitives._

  def car(d:SD): SD = d.asInstanceOf[List[SD]].head
  def cadr(d:SD): SD = d.asInstanceOf[List[SD]].tail.head

  def left_branch(mobile: SD): SD = car(mobile)

  def right_branch(mobile: SD): SD = cadr(mobile)

  def branch_length(branch: SD): SD = car(branch)

  def branch_structure(branch: SD): SD = cadr(branch)
}

// Part B
// Using your selectors, define a procedure total-weight that returns the total weight of a mobile.

object PartB {

  import PartA._

  def isMobile(data: SchemeData): Boolean = data match {
    case m: List[SchemeData] => m.length == 2
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
val branch1 = make_branch(length = 1.0, structure = 3.0)
val mobile1 = make_mobile(branch1, branch1)
assert(PartB.total_weight(mobile1) == 6.0)

val branch2 = make_branch(length = 1.0, mobile1)
val mobile2 = make_mobile(branch1,branch2)
assert(PartB.total_weight(mobile2) == 9.0)

// Part C Tests
assert(PartC.torque(branch1) == 3.0)
assert(PartC.torque(branch2) == 6.0)
assert(!PartC.isTorqueBalanced(branch1, branch2))
assert(PartC.isBalanced(mobile1))
assert(!PartC.isBalanced(mobile2))

val mobile3 = make_mobile(
  leftBranch  = make_branch(length = 9.0, mobile1),
  rightBranch = make_branch(length = 6.0, mobile2)
)

PartB.total_weight(mobile3)
PartC.torque(make_branch(length = 9.0, mobile1))
PartC.torque(make_branch(length = 6.0, mobile2))

assert(!PartC.isBalanced(mobile3))
