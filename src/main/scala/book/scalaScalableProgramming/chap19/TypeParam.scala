package book.scalaScalableProgramming.chap19

import scala.collection.immutable.Queue

/**
  * Created by arimuraterutoshi on 2017/02/22.
  */
object TypeParam {

  def main(args: Array[String]): Unit ={
    val q = Queue(1, 2, 3)
    val q1 = q enqueue 4
    print(q1)
  }

}
