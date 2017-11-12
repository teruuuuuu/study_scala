package book.scalaScalableProgramming.chap13


class Rocket{
  import Rocket.fuel
  private def canGoHomeAgain = fuel > 20
}

object Rocket {
  private def fuel = 10

  def chooseStrategy(rocket: Rocket): Unit ={
    // scalaのコンパニオンオブジェクトはprivateにアクセスできる
    if(rocket.canGoHomeAgain)
      goHome()
    else
      pickAStar()
  }

  def goHome(){}
  def pickAStar(){}
}


abstract class Fruit( val name: String, val color: String )

package object bobsdelights {
  def showFruit(fruit: Fruit): Unit ={
    import fruit._
    println(name + "s are " + color)
  }
}

object Fruits{
  object Apple extends Fruit("apple", "red")
  object Orange extends Fruit("orange", "orange")
  object Pear extends Fruit("pear", "yellowish")

  val menu = List(Apple, Orange, Pear)
}


object PrintMenu{
  def main(args: Array[String]): Unit ={
    import book.scalaScalableProgramming.chap13.bobsdelights._
    for(fruit <- Fruits.menu){
      showFruit(fruit)
    }
  }
}