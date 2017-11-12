package study.myParser

import util.parsing.combinator.JavaTokenParsers
import java.math.MathContext

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

// 各式を表すケースクラス。これを使ってAST(抽象構文木)を作ります。
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

class Evaluator extends ExpressionVisitor {
  // ケースクラスの抽出(unapply)を使って内包されている式を取り出してさらに訪問する
  override def visit(e:Expression) : BigDecimal = e match {
    case Value(digit) => digit
    case Add(l,r) => l.accept(this) + r.accept(this)
    case Sub(l,r) => l.accept(this) - r.accept(this)
    case Multiply(l,r) => l.accept(this) * r.accept(this)
    case Divide(l,r) => l.accept(this) / r.accept(this)
    case Minus(e) => e.accept(this) * -1
    case Plus(e) => e.accept(this)
    case Parenthesized(e) => e.accept(this)
  }
}

object Main {
  def main(args:Array[String]){
    val parser = new ExprParser
    val parseResult = parser.parse("(((0.1 + -1.2 * 3.4) * -3.3)/ 4.3) + 5.9")
    println(parseResult)
    if ( parseResult.successful ){
      val evalResult = parseResult.get.accept(new Evaluator)
      println(evalResult)
    }
  }
}