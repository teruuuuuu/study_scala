package book.scala_text

class collection {
  def arrTest: Unit ={
    val arr = Array[Int](1, 2, 3, 4, 5)
    arr(0) = arr(4)
    arr(4) = 2
    print(arr.mkString)
  }
  def swapArray[T](arr: Array[T])(i: Int, j: Int): Unit = {
    val tmp = arr(i)
    arr(i) = arr(j)
    arr(j) = tmp
  }

  def listTest: Unit = {
    val a = List(1,2,3,4,5)
    val b = List(6,7,8,9)
    println((a ++ b) mkString(","))
    println(a zip(b))
  }
  def joinByComma(start: Int, end: Int): String = {
    (start to end).mkString(",")
  }

  def foldLeftTest: Unit ={
    println(List(1, 2, 3).foldLeft(0)((x, y) => x + y))
  }
  def reverse[T](list: List[T]): List[T] = list.foldLeft(Nil: List[T])((a, b) => b :: a)

  def sum(list: List[Int]): Int = list.foldRight(0){(x, y) => x + y}

  def mkString[T](list: List[T])(sep: String): String = list match {
    case Nil => ""
    case x::xs => xs.foldLeft(x.toString){(x, y) => x + sep + y}
  }

  def filterTest: Unit = {
    println(List(1, 2, 3, 4, 5).filter(x => x % 2 == 1))
  }
  def findTest: Unit = {
    println(List(1, 2, 3, 4, 5).find(x => x % 2 == 1))
  }

  def flatMapTest: Unit = {
    println(List(1, 2, 3).flatMap{e => List(4, 5).map(g => e * g)})
  }
}

object collection {

  def main(args:Array[String]): Unit ={
    val ob = new collection();
    ob.listTest
    println(ob.joinByComma(3,10))
    ob.foldLeftTest
    println(ob.reverse(List(1,2,3,4,5)).mkString(","))

    println(ob.sum(List(1,2,3,4,5)))
    ob.filterTest
    ob.findTest
    ob.flatMapTest

  }
}