package book.scalaScalableProgramming.chap24

object CollectionAPI {


  def main(args: Array[String]): Unit ={

    List(1,2,3) map(_+1)

    val xs = List(1,2,3,4,5)
    xs.view(0,3)
    print(xs grouped 3 next)
  }
}
