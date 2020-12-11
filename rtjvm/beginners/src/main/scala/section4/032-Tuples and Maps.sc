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
      h) if there is a social connect between two people
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
def addUser(sc: SocialNetwork, person: String): SocialNetwork = sc + (person -> Set.empty)

// b) Remove a person from the network
def removeUser(sc: SocialNetwork, person: String): SocialNetwork = {
  sc.removed(person).map(p => p._1 -> p._2.filter(_ != person))
}

//
// Tests!
//
val test1 = addUser(Map.empty, "Bob")
val test2 = addUser(test1, "Sally")

assert(test2.keys.toList.length == 2)
assert(test2.contains("Bob"))
assert(test2.contains("Sally"))
