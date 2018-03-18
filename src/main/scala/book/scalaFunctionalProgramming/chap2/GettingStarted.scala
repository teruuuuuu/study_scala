package book.scalaFunctionalProgramming.chap2

object GettingStarted {
  def abs(n: Int) =
    if(n < 0) -n
    else n

  private def formatAbs(x: Int) = {
    val msg = "The absolute value of %d is %d"
    msg.format(x, abs(x))
  }

  def factorial(n: Int): Int = {
    def go(n: Int, acc: Int): Int =
      if(n <= 0) acc
      else go(n-1, n*acc)
    go(n, 1)
  }

  def fibo(n: Int): Int = {
    def go(n: Int): Int = {
      if (n == 1 || n == 2) 1
      else go(n-1) + go (n-2)
    }
    go(n)
  }

  def findFirst[A](as: Array[A], p: A => Boolean): Int = {
    @annotation.tailrec
    def loop(n: Int) : Int =
      if(n >= as.length) -1
      else if(p(as(n))) n
      else loop(n+1)

    loop(0)
  }

  def isSorted[A](as: Array[A], orderd: (A,A) => Boolean): Boolean = {
    @annotation.tailrec
    def loop(n: Int) : Boolean =
      if(n >= as.length - 1) true
      else if(orderd(as(n), as(n+1))) loop(n+1)
      else false
    loop(0)
  }

  def partial1[A,B,C](a:A, f: (A,B) => C): B => C =
    (b: B) => f(a, b)

  def curry[A,B,C](f: (A,B) => C): A => (B => C) =
    (a:A) => ((b:B) => f(a, b))

  def uncurry[A,B,C](f: A=> B => C): (A, B) => C =
    (a: A, b: B) => f(a)(b)

  def compose[A,B,C](f: B=>C, g:A=>B): A=> C =
    (a:A) => f(g(a))

  def main(args: Array[String]) = {
    println(formatAbs(-42))
    println("the factorial of 14 is " + factorial(14))
    println("the fibonach of 14 is " + fibo(14))
    println("find first List(1,2,3,4,5), 2 = " + findFirst(Array("1","2","3","4","5"), (k: String) => k.equals("2")))

    println("is sorted List(1,2,4,3,5) = " + isSorted(Array(1,2,4,3,5), (a: Int, b: Int) => a <= b))
    println("is sorted List(1,2,3,5) = " + isSorted(Array(1,2,3,5), (a: Int, b: Int) => a <= b))

    val comp2 = partial1(2, (a: Int, b: Int) => a <= b)
    println("comp2 5 = " + comp2(5))


    val plus2 = curry((a:Int, b:Int) => a+b)(2)
    println("plus2 5 = " + plus2(5))

    val sum = (a:Int) => (b:Int) => a + b
    val uncurried = uncurry(sum)
    println("sum(1)(2)", sum(1)(2))
    println("uncurry(1,2)", uncurried(1,2))

    println("compose=" + compose((a:Int) => 2+a, (b:Int) => b * b)(3))
  }

}
