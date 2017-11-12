package book.scalaScalableProgramming.chap33

import scala.util.parsing.combinator._

class JSON extends JavaTokenParsers {
  def value: Parser[Any] = obj | arr |
                           stringLiteral |
                           floatingPointNumber |
                           "null" | "true" | "false"
  def obj : Parser[Any] = "{"~repsep(member, ",")~"}"
  def arr : Parser[Any] = "["~repsep(value, ",")~"]"
  def member: Parser[Any] = stringLiteral~":"~value
}
import java.io.FileReader

object ParseJSON extends JSON {
  def main(args: Array[String]): Unit ={
    val reader = new FileReader("src/main/scala/book.scalaScalableProgramming/chap33/test.json")
    println(parseAll(value, reader))
  }
}