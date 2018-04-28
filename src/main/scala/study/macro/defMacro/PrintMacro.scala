package study.`macro`.defMacro

import scala.reflect.macros.Context


object PrintMacro {
  def p(msg: String): Unit = macro impl

  def impl(c: Context)(msg: c.Expr[String]) = {
    import c.universe._
    q"""
        println("" + ${msg})
      """
  }
}
