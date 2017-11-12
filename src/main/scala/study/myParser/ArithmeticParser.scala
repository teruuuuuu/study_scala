package study.myParser

import scala.util.parsing.combinator._
import study.myParser.workspace.ParseExpr.parseAll

case class Exp(tl: String, op: String, tr:String)
case class Term(fac: String, op: String)
case class Factor(num: String, exp: String)

object Arith extends JavaTokenParsers{
  def expr: Parser[Any] = term~rep("+"~term | "-"~term)
  def term: Parser[Any] = factor~rep("*"~factor | "/"~factor)
  def factor: Parser[Any] = floatingPointNumber | "("~expr~")"

  def apply(input: String): Either[String, Any] = parseAll(expr, input) match {
    case Success(postalCodeData, next) => Right(postalCodeData)
    case NoSuccess(errorMessage, next) => Left(s"$errorMessage on line ${next.pos.line} on column ${next.pos.column}")
  }

}

object ParseExpr{
  def main(args: Array[String]): Unit ={
    val input1 = "2 + (3 + 7)"
    println("input: " + input1)
    val parse1 = Arith(input1)

    println(parse1)

    val input2 = "2 + (3 + 7))"
    println("input: " + input2)
    println(Arith(input2))

  }
}