package book.scala_text


class function {

}

object function {
  val add = new Function2[Int, Int, Int]{
    def apply(x: Int, y: Int): Int = x + y
  }
  val add2 = (x: Int, y: Int) => x + y
  val addCurried = (x: Int) => ((y: Int) => x + y)

  def double(n: Int, f: Int => Int): Int = {
    f(f(n))
  }


  val addOne = addCurried(1)
  def addOneTwice(n: Int) = double(n, addOne)

  def main(args: Array[String]): Unit ={
    println(add(3, 2))
    println(addOne(2))
    println(addOneTwice(2))
  }

}