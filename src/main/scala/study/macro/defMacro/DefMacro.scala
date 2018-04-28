package study.`macro`.defMacro

import scala.language.experimental.macros

case class Person(val name: String, val age: Int)

object DefMacro {
  import PrintMacro._
  import DebugMacro._
  import AccesserMacro._

  def main(args: Array[String]) = {
    println(DebugMacro)
    println(p("test"))
    println(debug("test"))
    println(debug(Person("mikel", 30)))
    println(accesser(Person("mikel", 30), "name"))
    println(accesser(Person("mikel", 30), "age"))
  }
}