package book.scalaScalableProgramming.chap16

/**
  * Created by arimuraterutoshi on 2017/02/05.
  */
class ListTest {

}

object ListTest{
  def main(args: Array[String]): Unit ={

//    val a = List(3, 5, 2, 16, 7 , 9)
//    println(isortC(a))

    println(isortC(append(List(3,5,9), List(2,7,12))))
    println(msort((x:Int, y: Int) => x < y)(append(List(3,5,9), List(2,7,12))))
  }

  def isort(xs: List[Int]): List[Int] =
    if(xs.isEmpty) Nil
    else insert(xs.head, isort(xs.tail))

  def insert(x: Int, xs: List[Int]): List[Int] =
    if(xs.isEmpty || x <= xs.head) x :: xs
    else xs.head :: insert(x, xs.tail)

  def isortC(xs: List[Int]): List[Int] = xs match {
    case List() => List()
    case x :: xs1 => insertC(x, isort(xs1))
  }

  def insertC(x: Int, xs: List[Int]): List[Int] = xs match {
    case List() => List(x)
    case y :: ys => if(x <= y) x:: xs
    else y :: insert(x, ys)
  }

  def append[T](xs: List[T], ys: List[T]): List[T] =
    xs match {
      case List() => ys
      case x :: xs1 => x :: append(xs1, ys)
    }

  def msort[T](less: (T, T) => Boolean)
              (xs: List[T]): List[T] = {
    def merge(xs: List[T], ys: List[T]): List[T] =
      (xs, ys) match {
        case (Nil, _) => ys
        case (_, Nil) => xs
        case (x :: xs1, y :: ys1) =>
          if(less(x, y)) x :: merge(xs1, ys)
          else y :: merge(xs, ys1)
      }
    val n = xs.length / 2
    if (n == 0) xs
    else{
      val (ys, zs) = xs splitAt n
      merge(msort(less)(ys), msort(less)(zs))
    }
  }
}
