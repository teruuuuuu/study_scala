package book.scalaScalableProgramming.chap11

/**
  * Created by arimuraterutoshi on 2017/01/15.
  */
class ClassLayer {

}

object ClassLayer{

  def main(args: Array[String]): Unit ={
    println("42.toString.getClass :" + 42.toString.getClass)
    println("42.hashCode.getClass :" + 42.hashCode.getClass)

    // objectとAnyRefは交換可能だがAnyRefで統一したほうが良い
    val ob:Object = new Object()
    val ob2:AnyRef = new Object()

    val ref:AnyRef = new AnyRef()
    val ref2:AnyRef = new Object()



  }
}