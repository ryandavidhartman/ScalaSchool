abstract class Food(val name: String) {
  override def toString = name
}

class Recipe(
  val name: String,
  val ingredients: List[Food],
  val instructions: String
)

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

  case class FoodCategory(name: String, foods: List[Food])

  private val categories = List(
    FoodCategory("fruits", List(Apple,Orange)),
    FoodCategory("misc", List(Cream, Sugar))
  )

  def allCategories = categories
}

object SimpleBrowser {
  def recipesUsing(food: Food): List[Recipe] =
    SimpleDatabase.allRecipes.filter(r => r.ingredients.contains(food))

  def displayCategory(category: SimpleDatabase.FoodCategory): Unit = {
    println(category)
  }
}

//val apple = SimpleDatabase.foodNamed("Apple").get
//val appleRecipes = SimpleBrowser.recipesUsing(apple)

//println(s"Here are the apple recipes: $appleRecipes")

//SimpleDatabase.allCategories.map(SimpleBrowser.displayCategory)



