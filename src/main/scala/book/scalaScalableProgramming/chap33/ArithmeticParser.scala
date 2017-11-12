package book.scalaScalableProgramming.chap33


import scala.util.parsing.combinator._

class Arith extends JavaTokenParsers{
  def expr: Parser[Any] = term~rep("+"~term | "-"~term)
  def term: Parser[Any] = factor~rep("*"~factor | "/"~factor)
  def factor: Parser[Any] = floatingPointNumber | "("~expr~")"
}

object ParseExpr extends Arith{
  def main(args: Array[String]): Unit ={
    val input1 = "2 + (3 + 7)"
    println("input: " + input1)
    val parse1 = parseAll(expr, input1)
    println(parse1)
    val pg = parse1.get
    println(pg)
/*
    val input2 = "2 + (3 + 7))"
    println("input: " + input2)
    val parse2 = parseAll(expr, input2)
    println(parse2)
*/
  }
}