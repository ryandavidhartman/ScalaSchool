abstract class Food(val name: String) {
  override def toString = name
}

class Recipe(
  val name: String,
  val ingredients: List[Food],
  val instructions: String
)

// Say Food and Recipes represent, entities that will be
// persisted in the database.  In the real world you'd
// need to add primary keys, etc. but let's skip that for
// now.

// lets make some test data

object Apple extends Food("Apple")

object Orange extends Food("Orange")

object Cream extends Food("Cream")

object Sugar extends Food("Sugar")

object FruitSalad extends Recipe(
  "fruit salad",
  List(Apple, Orange, Cream, Sugar),
  "Stir it all together."
)

// Now lets make a mock database
object SimpleDatabase {
  def allFoods = List(Apple, Orange, Cream, Sugar)

  def foodNamed(name: String): Option[Food] =
    allFoods.find(_.name == name)

  def allRecipes = List[Recipe](FruitSalad)
}

object SimpleBrowser {
  def recipesUsing(food: Food): List[Recipe] =
    SimpleDatabase.allRecipes.filter(r => r.ingredients.contains(food))
}


val apple = SimpleDatabase.foodNamed("Apple").get
val appleRecipes = SimpleBrowser.recipesUsing(apple)

println(s"Here are the apple recipes: $appleRecipes")




