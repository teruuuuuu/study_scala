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

class JSONView extends JavaTokenParsers {
//  def value: Parser[Any] = obj | arr |
//    stringLiteral |
//    floatingPointNumber |
//    "null" | "true" | "false"
  def obj : Parser[Map[String, Any]] =
    "{" ~> repsep(member, ",") <~ "}" ^^ (Map() ++ _)

  def arr : Parser[List[Any]] =
    "[" ~> repsep(value, ",") <~ "]"

  def member: Parser[(String, Any)] =
    stringLiteral ~":"~ value ^^
      {case name ~":" ~value => (name, value)}

  def value: Parser[Any] = (
    obj
      | arr
      | stringLiteral
      | floatingPointNumber ^^ (_.toDouble)
      | "null" ^^ (x => null)
      | "true" ^^ (x => true)
      | "false" ^^ (x => false)
  )

}

import java.io.FileReader

object J extends JSON {def p(reader: FileReader) = parseAll(value, reader)}
object JV extends JSONView { def p(reader: FileReader) = parseAll(value, reader)}

object ParseJSON extends JSONView {
  def main(args: Array[String]): Unit ={
    val reader = new FileReader("src/main/scala/book/scalaScalableProgramming/chap33/test.json")
    println(parseAll(value, reader))
  }
}