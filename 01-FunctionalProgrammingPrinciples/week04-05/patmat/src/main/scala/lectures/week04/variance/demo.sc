/*
From
https://www.freecodecamp.org/news/understand-scala-variances-building-restaurants/
 */


trait Food {
  def name: String
}

class Meat(val name: String) extends Food
class Vegetable(val name: String) extends Food
class WhiteMeat(override val name: String) extends Meat(name)


// Food <- Meat
val beef = new Meat("beef")

// Food <- Meat <- WhiteMeat
val chicken = new WhiteMeat("chicken")
val turkey = new WhiteMeat("turkey")

// Food <- Vegetable
val carrot = new Vegetable("carrot")
val pumpkin = new Vegetable("pumpkin")


// make a COVARIANT type recipe
trait Recipe[+A] {
  def name: String
  def ingredients: List[A]
}

case class GenericRecipe(ingredients: List[Food]) extends Recipe[Food] {
  def name: String = s"Generic recipe based on ${ingredients.map(_.name)}"
}

case class MeatRecipe(ingredients: List[Meat]) extends Recipe[Meat] {
  def name: String = s"Meat recipe based on ${ingredients.map(_.name)}"
}

case class WhiteMeatRecipe(ingredients: List[WhiteMeat]) extends Recipe[WhiteMeat] {
  def name: String = s"Meat recipe based on ${ingredients.map(_.name)}"
}

// Recipe[Food]: Based on Meat or Vegetable
val mixRecipe = GenericRecipe(List(chicken, carrot, beef, pumpkin))
// Recipe[Food] <- Recipe[Meat]: Based on any kind of Meat
val meatRecipe = MeatRecipe(List(beef, turkey))
// Recipe[Food] <- Recipe[Meat] <- Recipe[WhiteMeat]: Based only on WhiteMeat
val whiteMeatRecipe = WhiteMeatRecipe(List(chicken, turkey))



/*
Ches if CONTRAVARIANT!
Since Chef is contravariant,
Chef[Food] is a subclass of Chef[Meat]
that is a subclass of Chef[WhiteMeat].
This means that the relationship between subtypes is the inverse
of its component type Food.
 */
trait Chef[-A] {
  def specialization: String
  def cook(recipe: Recipe[A]): String
}

class GenericChef extends Chef[Food] {
  val specialization = "All food"
  override def cook(recipe: Recipe[Food]): String = s"I made a ${recipe.name}"
}

class MeatChef extends Chef[Meat] {
  val specialization = "Meat"
  override def cook(recipe: Recipe[Meat]): String = s"I made a ${recipe.name}"
}
class WhiteMeatChef extends Chef[WhiteMeat] {
  override val specialization = "White meat"
  def cook(recipe: Recipe[WhiteMeat]): String = s"I made a ${recipe.name}"
}


// Chef[WhiteMeat]: Can cook only WhiteMeat
val giuseppe = new WhiteMeatChef
giuseppe.cook(whiteMeatRecipe)
// fails giuseppe.cook(meatRecipe)
// fails giuseppe.cook(mixRecipe)

// Chef[WhiteMeat] <- Chef[Meat]: Can cook only Meat
val alfredo = new MeatChef
alfredo.cook(meatRecipe)
alfredo.cook(whiteMeatRecipe)
// fails alfredo.cook(mixRecipe)

// Chef[WhiteMeat]<- Chef[Meat] <- Chef[Food]: Can cook any Food
val mario = new GenericChef
mario.cook(mixRecipe)
mario.cook(meatRecipe)
mario.cook(whiteMeatRecipe)



