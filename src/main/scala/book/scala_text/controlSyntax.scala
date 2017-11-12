package book.scala_text

import java.util.Locale


class controlSyntax {
  def p2(a: Int) = Math.pow(a, 2)
  def sannkaku(a: Boolean) = if(a) println("trueです") else println("falseです")
  def loopFrom0To9(): Unit = {
    var i = 0
    do {
      println(i)
      i += 1
    } while(i < 10)
  }

  def countTriangle = {
    var ret = 0
    for(x <- 1 to 10; y <- 1 until x; z <- 1 until y; if p2(x) == (p2(y) + p2(z))){
      ret = ret + 1
    }
    ret
  }

  def partMatch = {
    val lst = List("A", "B", "C", "D", "E")
    lst match {
      case List("A", b, c, d, e) =>
        println("b = " + b)
        println("c = " + c)
        println("d = " + d)
        println("e = " + e)
      case _ =>
        println("nothing")
    }
  }

  def randomChar = {
    for(i <- 1 to 5) {
      val s = new scala.util.Random(new java.security.SecureRandom()).alphanumeric.take(5).toList match {
        case List(a,b,c,d,_) => List(a,b,c,d,a).mkString
      }
      println(s)
    }
  }

  def typeMatch(obj: Any): Unit = {
    obj match {
      case v:java.lang.Integer =>
        println("Integer!")
      case v:String =>
        println(v.toUpperCase(Locale.ENGLISH))
      case _ =>
        println("not int or string")
    }
  }

}



object controlSyntax{

  def main(args: Array[String]): Unit ={

    val ob = new controlSyntax()
    ob.sannkaku(true)
    ob.loopFrom0To9
    println("1辺が10以下の直角三角形の数: " + ob.countTriangle)
    ob.partMatch
    ob.randomChar
    ob.typeMatch(1)

  }
}