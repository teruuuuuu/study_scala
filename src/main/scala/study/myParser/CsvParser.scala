package study.myParser

import scala.util.parsing.combinator.RegexParsers


case class CSV(header: Header, rows: Seq[Record])
case class Header(names: Seq[String])
case class Record(fields: Seq[String])

object CSVParser extends RegexParsers {

  override def skipWhitespace = false

  // file = [header CRLF] record (CRLF record)* [CRLF]
  def file = opt(header <~ CRLF) ~ repsep(record, CRLF) <~ opt(CRLF) ^^ {
    case Some(header) ~ records => CSV(header, records)
    case None ~ records         => CSV(Header(List()), records)
  }

  // header = name (COMMA name)*
  def header = repsep(name, comma) ^^ { names => Header(names.map(_.toString)) }

  // record = field (COMMA field)*
  def record = repsep(field, comma) ^^ { fields => Record(fields.map(_.toString)) }

  // name = field
  def name = field

  // field = (escaped | non-escaped)
  def field = escaped | nonEscaped

  // escaped = DQUOTE (TEXTDATA | COMMA | CR | LF | 2DQUOTE)* DQUOTE
  def escaped = doubleQuote ~> (textdata | comma | CR | LF | twoDoubleQuote).* <~ doubleQuote ^^ { _.mkString }

  // 2DQUOTE = DQUOTE DQUOTE
  def twoDoubleQuote = doubleQuote ~ doubleQuote ^^ { case d1 ~ d2 => d1 + d2 }

  // non-escaped = TEXTDATA*
  def nonEscaped = textdata.* ^^ { _.mkString }

  // COMMA = '\u002C'
  def comma = ","

  // CR = ''
  def CR = "\r"

  // DQUOTE = ''
  def doubleQuote = "\""

  // LF = ''
  def LF = "\n"

  // CRLF = CR LF
  def CRLF = CR ~ LF

  // TEXTDATA = #'[\u0020-\u0021\u0023-\u002B\u002D-\u007E]'
  def textdata = """[\u0020-\u0021\u0023-\u002B\u002D-\u007E]""".r

  def apply(input: String): Either[String, Any] = parseAll(file, input) match {
    case Success(csvData, next)        => Right(csvData)
    case NoSuccess(errorMessage, next) => Left(s"$errorMessage on line ${next.pos.line} on column ${next.pos.column}")
  }

  def main(args:Array[String]): Unit ={
    println(CSVParser("a,b,c\r\n1,2,3\r\nx,y,z"))
  }
}