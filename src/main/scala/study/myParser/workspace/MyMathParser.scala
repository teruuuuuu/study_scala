package study.myParser.workspace

import java.math.MathContext

import study.myParser.workspace

import scala.util.parsing.combinator.JavaTokenParsers

class Arith extends JavaTokenParsers{
  def expr: Parser[Any] = term~rep("+"~term | "-"~term)
  def term: Parser[Any] = factor~rep("*"~factor | "/"~factor)
  def factor: Parser[Any] = floatingPointNumber | "("~expr~")"
}

// 式を訪問するビジター
trait ExpressionVisitor {
  def visit(e:Expression):BigDecimal
}

// 式を表すトレイト
trait Expression {
  def accept(visitor:ExpressionVisitor):BigDecimal = {
    visitor.visit(this)
  }
}

case class Value(digit:BigDecimal) extends Expression
case class Add(expr1:Expression, expr2:Expression) extends Expression
case class Sub(expr1:Expression, expr2:Expression) extends Expression
case class Plus(expr:Expression) extends Expression
case class Minus(expr:Expression) extends Expression
case class Parenthesized(expr:Expression) extends Expression
case class Multiply(expr1:Expression, expr2:Expression) extends Expression
case class Divide(expr1:Expression, expr2:Expression) extends Expression

// 式のパーサ
class ExprParser extends JavaTokenParsers {
  def parse(data:String) = parseAll(expression, data)

  // expression ::= term { "+" term | "-" term }
  def expression : Parser[Expression] = term~rep("+"~term | "-"~term) ^^ {
    case opr1~lists => {
      var operand1 = opr1
      lists.foreach {
        l => l match {
          case "+"~f => { operand1 = Add(operand1, f) }
          case "-"~f => { operand1 = Sub(operand1, f) }
        }
      }
      operand1
    }
  }

  // term ::= factor { "*" factor | "/" factor }
  def term : Parser[Expression] = factor~rep("*"~factor | "/"~factor) ^^ {
    case opr1~lists => {
      var operand1 = opr1
      lists.foreach {
        l => l match {
          case "*"~f => { operand1 = Multiply(operand1, f) }
          case "/"~f => { operand1 = Divide(operand1, f) }
        }
      }
      operand1
    }
  }

  // factor ::= unary
  def factor : Parser[Expression] = unary

  // unary :: = "+" unary | "-" unary | primary
  def unary : Parser[Expression] =
    ("+"|"-")~unary ^^ {
      case "+"~u => Plus(u)
      case "-"~u => Minus(u)
    }| primary

  // primary ::= "(" expression ")" | value
  def primary : Parser[Expression] =
    "("~expression~")" ^^ {
      case lbrace~expr~rbrace => Parenthesized(expr)
    }|value

  // value ::= "[0-9]+"
  // floatingPointNumberは浮動小数点を表す正規表現のParser
  def value : Parser[Expression] =
  floatingPointNumber ^^ {
    n => Value(BigDecimal(n, MathContext.DECIMAL32))
  }
}

object ParseExpr extends Arith{
  def main(args: Array[String]): Unit ={
    val input1 = "2 + (3 + 7)"
/*
    println("input: " + input1)
    val parse1 = parseAll(expr, input1)
    println(parse1)
    val pg = parse1.get
    println(pg)
*/

    val parser = new ExprParser
    val parseResult = parser.parse(input1)
    println(parseResult)
  }
}