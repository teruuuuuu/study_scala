package study.`macro`.defMacro

import language.experimental.macros
import scala.reflect.macros.whitebox.Context

object AccesserMacro {
  def accesser(obj: Any, property: String): Any = macro impl

  def impl(c: Context)(obj: c.Expr[Any], property: c.Expr[String]) = {
    import c.universe._
    val Expr(Literal(Constant(propString: String))) =  property
    Select(obj.tree, TermName(propString))
  }
}