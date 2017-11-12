package study.myParser

import scala.util.parsing.combinator.RegexParsers


case class PostalCode(threeDigit: String, fourDigit: String){

  override def toString: String = {
    "threeDigit:" + this.threeDigit + " fourDigit:" + this.fourDigit
  }
}

object PostalCodeParser extends RegexParsers {
  def postalCode = """\d{3}""".r ~ "-" ~ """\d{4}""".r ^^ { case (threeDigit ~ "-" ~ fourDigit) => PostalCode(threeDigit, fourDigit) }
  def apply(input: String): Either[String, PostalCode] = parseAll(postalCode, input) match {
    case Success(postalCodeData, next) => Right(postalCodeData)
    case NoSuccess(errorMessage, next) => Left(s"$errorMessage on line ${next.pos.line} on column ${next.pos.column}")
  }

  def main(args:Array[String]): Unit ={
    println(PostalCodeParser("123-4567"))
  }
}

