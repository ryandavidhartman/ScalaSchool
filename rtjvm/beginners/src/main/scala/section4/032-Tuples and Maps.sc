import scala.annotation.tailrec

/*
Exercises:

1. What would happen if  I had two entries in a map "Jim" -> 555 and "JIM" -> 999 in a map
   and I mapped over the Map setting the key names to lower case?

2. Make an overly simplified "social network" based on Maps.

   The network will be a Map of people (strings) to a list of their
   friends (also strings)
      a) we should be able to add a new person
      b) remove a person
      c) add a friend to someone (friends are mutual)
      d) remove a friend from someone (mutual?)

      Stats:
      e) number of friends of a person
      f) person with the most friends
      g) how many people have no friends
      h) if there is a social connection between two people
         (direct or not)
 */

//
// Exercise 1:
//

// It will keep the last entry
val phoneBook = Map("Jim" -> 555, "Daniel" -> 789, "JIM" -> 999)
val test = phoneBook.map(tuple => tuple._1.toLowerCase -> tuple._2)
assert(test("jim") == 999)

//
// Exercise 2:
//
type SocialNetwork = Map[String, Set[String]]

// a) Add new person to the network
def addUser(sn: SocialNetwork, person: String): SocialNetwork = {
  if(sn.contains(person))
    sn
  else
    sn + (person -> Set.empty)
}

def addUsers(sn: SocialNetwork, people: Set[String]): SocialNetwork = {
  if(people.isEmpty)
    sn
  else
    addUsers(addUser(sn, people.head), people.tail)
}

// b) Remove a person from the network
def removeUser(sn: SocialNetwork, person: String): SocialNetwork = {
  sn.removed(person).map(p => p._1 -> p._2.filter(_ != person))
}

// c) add a friend to someone (friends are mutual)
def addFriend(sn: SocialNetwork, user: String, newFriend: String): SocialNetwork = {
  val newSN = addUsers(sn, Set(user, newFriend))

  newSN.updated(user, newSN(user) + newFriend)
       .updated(newFriend, newSN(newFriend) + user)
}

// d) remove a friend from someone (mutual)
def removeFriend(sn: SocialNetwork, user: String, exFriend: String): SocialNetwork = {
  sn.updated(user, sn(user) - exFriend)
    .updated(exFriend, sn(exFriend) - user)
}

// e) number of friends of a person
def numberOfFriend(sn: SocialNetwork, user: String): Int = {
  sn(user).size
}

// f) person with the most friends
def mostPopular(sn: SocialNetwork): String = {
  sn.toList.sortWith((r, l) => r._2.size > l._2.size).head._1
}

// g) how many people have no friends
def numberOfPeopleWithNoFriends(sn: SocialNetwork): Int = {
  sn.filter(p => p._2.isEmpty).size
}

//  h) if there is a social connection between two people
def isConnectedTo(sn: SocialNetwork, user: String, target: String): Boolean = {

  @tailrec
  def helper(friends: Set[String], alreadyLookedAt: Set[String]): Boolean = friends match {
    case f if f.isEmpty => false
    case f if f.contains(target) => true
    case _ => {
      val first = friends.head
      val rest = friends.tail
      if(alreadyLookedAt.contains(first))
        helper(rest, alreadyLookedAt)
      else
        helper(rest ++ sn(first), alreadyLookedAt + first)
    }
  }

  helper(sn(user) + user, Set.empty)
}

def isFriendAConnectedTo(friendB: String, traversedList: Set[String]) : Boolean = {
  val curMappings = mappings(friendB)
  if (curMappings.isEmpty) false
  else if (curMappings.contains(friendA)) true
  else curMappings.aggregate(false)(
    (_, entry) => {
      if (traversedList.contains(entry)) false
      else isFriendAConnectedTo(entry, traversedList + friendB )
    },
    (currentState, entry) => currentState || entry)
}
isFriendAConnectedTo(friendB, Set[String]())

//
// Tests!
//
val testSocialNetwork = addUsers(Map.empty,Set("Sally", "Bob"))

assert(testSocialNetwork.keys.toList.length == 2)
assert(testSocialNetwork.contains("Bob"))
assert(testSocialNetwork.contains("Sally"))

val removeUserTest = removeUser(testSocialNetwork, "Sally")
assert(removeUserTest.keys.toList.length == 1)
assert(removeUserTest.contains("Bob"))
assert(!removeUserTest.contains("Sally"))

val addTest1 = addFriend(testSocialNetwork, "Bob", "Sally")
val addTest2  = addFriend(addTest1,"Bob", "Jim")
val addTest3 = addFriend(addTest2, "Keith", "Brad")

val removeFriendTest1 = removeFriend(addTest3, "Bob", "Jim")

assert(mostPopular(addTest3) == "Bob")

val noFriendsTest = addUsers(addTest3, Set("Dan", "Steve"))
assert(numberOfPeopleWithNoFriends(noFriendsTest) == 2)

assert(isConnectedTo(addTest3, "Bob", "Sally") == true)

val connectedTest = addFriend(addTest3, "Jim", "Keith")
assert(isConnectedTo(connectedTest, "Bob", "Brad") == true)
