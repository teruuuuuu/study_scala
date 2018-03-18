package book.scalaFunctionalProgramming.chap3

sealed trait List[+A]
case object Nil extends List[Nothing]
case class Cons[+A](head: A, tail: List[A]) extends List[A]


object Structure {
  def sum(x: List[Int]):Int = x match {
    case Nil => 0
    case Cons(x, xs) => x + sum(xs)
  }

  object List {
    def sum(ints: List[Int]): Int = ints match {
      case Nil => 0
      case Cons(x, xs) => x + sum(xs)
    }

    def product(ds: List[Double]): Double = ds match {
      case Nil => 1.0
      case Cons(0.0, _) => 0.0
      case Cons(x, xs) => x * product(xs)
    }

    def apply[A](as: A*): List[A] =
      if (as.isEmpty) Nil
      else Cons(as.head, apply(as.tail:_*))
  }
  def tail[A](list: List[A]): List[A] = list match {
    case Cons(x, xs) => xs
    case _ => List[A]()
  }

  def setHead[A](list: List[A], head:A): List[A] = list match {
    case Cons(x, xs) => Cons(head, xs)
    case _ => List(head)
  }

  def drop[A](l: List[A], n: Int): List[A] = {
    def loop[A](l: List[A], n: Int):List[A] = n match {
      case n if n <= 0 => l
      case _ => loop(tail(l), n-1)
    }
    loop(l, n)
  }

  def dropWhile[A](l: List[A], f: A=>Boolean): List[A] = l match {
    case Cons(x, xs) if (f(x)) => dropWhile(xs, f)
    case _ => List[A]()
  }

  def foldRight[A,B](as: List[A], z:B)(f: (A,B) => B):B = as match {
    case Nil => z
    case Cons(x, xs) => f(x, foldRight(xs, z)(f))
  }

  def sum2(ns: List[Int]) =
    foldRight(ns, 0)((x,y) => x + y)

  def product2(ns: List[Double]) =
    foldRight(ns, 1.0)(_ * _)

  def length[A](l: List[A]): Int =
    foldRight(l, 0)((_,acc) => acc + 1)

  def foldLeft[A,B](l: List[A], z: B)(f: (B, A) => B): B = l match {
    case Nil => z
    case Cons(h,t) => foldLeft(t, f(z,h))(f)
  }

  def reverse[A](l: List[A]): List[A] =
    foldLeft(l, List[A]())((acc,h) => Cons(h,acc))

  def foldRightViaFoldLeft[A,B](l: List[A], z: B)(f: (A,B) => B): B =
    foldLeft(reverse(l), z)((b,a) => f(a,b))

//  def foldRightViaFoldLeft_1[A,B](l: List[A], z: B)(f: (A,B) => B): B =
//    foldLeft(l, (b:B) => b)((g,a) => b => g(f(a,b)))(z)

  def foldRightViaFoldLeft_1[A,B](l: List[A], z: B)(f: (A,B) => B): B =
    foldLeft(l, (b:B) => b)((g,a)=> b => g(f(a,b)))(z)

  def foldLeftViaFoldRight[A,B](l: List[A], z: B)(f: (B,A) => B): B =
    foldRight(l, (b:B) => b)((a,g) => b => g(f(b,a)))(z)


  def append[A](a1: List[A], a2: List[A]): List[A] =
    a1 match {
      case Nil => a2
      case Cons(h,t) => Cons(h, append(t, a2))
    }
  
  def concat[A](l: List[List[A]]): List[A] =
    foldRight(l, Nil:List[A])(append)

  def add1(l: List[Int]): List[Int] =
    foldRight(l, Nil:List[Int])((h,t) => Cons(h+1,t))

  def doubleToString(l: List[Double]): List[String] =
    foldRight(l, Nil:List[String])((h,t) => Cons(h.toString,t))

  val x = List(1,2,3,4,5) match {
    case Cons(x, Cons(2, Cons(4,_))) => x
    case Nil => 42
    case Cons(x, Cons(y, Cons(3, Cons(4,_)))) => x + y
    case Cons(h, t) => h + sum(t)
    case _ => 101
  }

  def main(arg: Array[String]) = {
    println("x=" + x)

    println("tail=" + tail(List(1,2,3,4,5)))

    println("setHead:" + setHead(List(1,2,3), 4))
    println("setHead:" + setHead(List(1), 4))
    println("setHead:" + setHead(List(), 4))


    println("drop:" + drop(List(1,2,3,4), 3))

    println("foldRightViaFoldLeft_1:" + foldRightViaFoldLeft_1(List(1,2,3,4), 5)( (a:Int,b:Int)=> a + b))

  }

}
