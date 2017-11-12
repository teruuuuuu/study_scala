package book.scalaPuzzer

object Q1 {

  def main(args: Array[String]): Unit ={
    println(List(1, 2).map{
      i => println("Hi"); i+1
    })

    println(List(1,2 ).map{println("Hi"); _+1})
  }
}
