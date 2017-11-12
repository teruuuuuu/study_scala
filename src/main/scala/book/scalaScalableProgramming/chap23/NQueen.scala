package book.scalaScalableProgramming.chap23


object NQueen {

  def main(args: Array[String]): Unit ={
    val q1 = queens(5)
    println(q1)
  }

  def queens(n: Int): List[List[(Int, Int)]] ={
    def placeQuees(k: Int): List[List[(Int, Int)]] =
      if(k == 0)
        List(List())
      else
        for{
          queens <- placeQuees( k -1)
          column <- 1 to n
          queen = (k, column)
          if isSafe(queen, queens)
        } yield queen :: queens

    def isSafe(queen: (Int, Int), queens: List[(Int, Int)]) =
      queens forall (q => !inCheck(queen, q))

    def inCheck(q1: (Int, Int), q2: (Int, Int)) =
      q1._1 == q2._1 ||
      q1._2 == q2._2 ||
        (q1._1 - q2._1).abs == (q1._2 - q2._2).abs

    placeQuees(n)
  }
}
