package book.scalaScalableProgramming.chap15


object ExprObj {
  def main(args: Array[String]): Unit ={
//    val v = Var("x")
//    val op = BinOp("+", Number(1), v)
//
//    op.copy(operator = "-")

//    println(simpliyfyTop(UnOp("-", Var("x"))))
//    println(simpliyfyTop(UnOp("-", UnOp("-", Var("34")))))

//    import math.{E, Pi}
//    val pi = math.Pi
//    var a = E match {
//      case Pi => "strage math? Pi = " + Pi
//      case pi => "strage math? Pi = " + pi
//      case _ => "OK"
//    }
//    println(a)

//    def tupleDemo(expr: Any) =
//      expr match {
//        case (a, b, c) => println("matched " + a + b + c)
//        case _ =>
//      }
//
//    tupleDemo(("a ", 3, "-tuple"))

//    def isIntIntMap(x: Any) = x match{
//      case m:Map[Int, Int] => true
//      case _ => false
//    }
//    println(isIntIntMap(Map(1 -> -1)))
//    println(isIntIntMap(Map("a" -> "b")))

//    def isStringArray(x: Any) = x match {
//      case a: Array[String] => "yes"
//      case _ => "no"
//    }
//    println(isStringArray(Array("abc")))
//    println(isStringArray(Array(1, 2, 3)))

//    println(simplifyAdd( BinOp("+", Number(1), Number(2))  ))
//    println(simplifyAdd( BinOp("+", Number(3), Number(3))  ))
//
//    println(describe(Var("3")))

//    val capitals = Map("France" -> "Paris", "Japan" -> "Tokyo")
//    println(opShow(capitals get "Japan"))
//    println(opShow(capitals get "America"))

//    val myTuple = (123, "abc")
//    val (number, string) = myTuple
//    println(number)
//    println(string)

//    val exp = new BinOp("*", Number(5), Number(1))
//    val BinOp(op, left, right) = exp
//    println(op)
//    println(left)
//    println(right)

    //println(withDefault(Some(10)))

    val f: ExprFormatter = new ExprFormatter
    val e1 = BinOp("*", BinOp("/", Number(1), Number(2)), BinOp("+", Var("x"), Number(1)))
    val e2 = BinOp("+", BinOp("/", Var("x"), Number(2)), BinOp("/", Number(1.5), Var("x")))
    val e3 = BinOp("/", e1, e2)

    def show(e: Expr) = println(f.format(e) + "\n\n")
    for(e <- Array(e1, e2, e3)) show(e)


}

  def simpliyfyTop(expr: Expr): Expr = expr match {
    case UnOp("-", UnOp("-", e)) => e
    case BinOp("+", e, Number(0)) => e
    case BinOp("*", e, Number(1)) => e
    case _ => expr
  }

  def simplifyAdd(e: Expr) = e match {
    //　パターンガードによる条件指定
    case BinOp("+", x, y) if x == y =>
      BinOp("*", x, Number(2))
    case _ => e
  }

  def simplifyAll(expr: Expr): Expr = expr match {
    case UnOp("-", UnOp("-", e)) => simplifyAll(e)
    case BinOp("+", e, Number(0)) => simplifyAll(e)
    case BinOp("*", e, Number(1)) => simplifyAll(e)
    case UnOp(op, e) => UnOp(op, simplifyAll(e))
    case BinOp(op, l, r) => BinOp(op, simplifyAll(l), simplifyAll(r))
    case _ => expr
  }

  def describe(e: Expr): String = (e: @unchecked) match{
    case Number(_) => "a number"
    case Var(_) => "a variable"
  }

  def opShow(x: Option[String]) = x match{
    case Some(s) => s
    case None => "?"
  }

  val withDefault: Option[Int] => Int = {
    case Some(x) => x
    case None => 0
  }
}

