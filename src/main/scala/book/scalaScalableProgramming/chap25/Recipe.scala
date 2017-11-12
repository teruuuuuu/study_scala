package book.scalaScalableProgramming.chap25

abstract class Food(val name: String){
  override def toString = name
}

class Recipe(
 val name :String,
 val ingredients: List[Food],
 val instructions: String
){
  override def toString = name
}

object Apple extends Food("Apple")
object Orange extends Food("Orange")
object Cream extends Food("Cream")
object Sugar extends Food("Sugar")
object FruitSalad extends Recipe(
  "fruit salad",
  List(Apple, Orange, Cream, Sugar),
  "Stir it all together")

trait FoodCategories{
  case class FoodCategory(name: String, foods:List[Food])
  def allCategories: List[FoodCategory]
}

abstract class Database extends FoodCategories{
  def allFoods: List[Food]
  def allRecipes: List[Recipe]
  def foodNamed(name: String) =
    allFoods.find( f => f.name == name)
}
//
//object SimpleDatabase extends Database {
//  def allFoods = List(Apple, Orange, Cream, Sugar)
//  def allRecipes: List[Recipe] = List(FruitSalad)
//
//  private var categories = List(
//    FoodCategory("fruits", List(Apple, Orange)),
//    FoodCategory("misc", List(Cream, Sugar)))
//
//  def allCategories = categories
//}

object SimpleDatabase extends Database with SimpleFoods with SimpleRecipes

object StudentDatabase extends Database{
  object FrozenFood extends Food("FrozenFood")
  object HeatItUp extends Recipe("heat it up",
    List(FrozenFood), "Microwave the 'food' for 10 minutes")
  def allFoods = List(FrozenFood)
  def allRecipes = List(HeatItUp)
  def allCategories = List(
    FoodCategory("edible", List(FrozenFood))
  )
}
abstract class Browser{
  val database: Database
  def recipesUsing(food: Food) =
    SimpleDatabase.allRecipes.filter(recipe =>
      recipe.ingredients.contains(food))

  def displayCategory(category: database.FoodCategory): Unit ={
    println(category)
  }
}

object SimpleBrowser extends Browser{
  val database = SimpleDatabase
}

trait SimpleFoods {
  object Pear extends Food("Pear")
  def allFoods = List(Apple, Pear)
  def allCategories = Nil
}

trait SimpleRecipes {
  this: SimpleFoods =>
  object FruitSalad extends Recipe(
    "fruit salad",
    List(Apple, Pear),
    "Mix it all together."
  )
  def allRecipes = List(FruitSalad)
}

object gotApples {
  def main(args: Array[String]): Unit ={
    val db: Database =
      if(args(0) == "student")
        StudentDatabase
      else
        SimpleDatabase
    object browser extends Browser{
      val database = db
    }

    val apple = SimpleDatabase.foodNamed("Apple").get
    for(recipe <- browser.recipesUsing(apple))
      println(recipe)

  }
}

