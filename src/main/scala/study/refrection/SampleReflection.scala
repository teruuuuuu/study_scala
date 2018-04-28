package study.refrection

import scala.reflect.macros.Context
import scala.language.experimental.macros
import scala.reflect.runtime.{universe => ru}




case class Person(name: String)
case class Purchase(name: String, orderNumber: Int, var shipped: Boolean)
class E {
  type T
  val x: Option[T] = None
}

class C extends E
class D extends E

object SampleReflection {

  val l = List(1,2,3)
  def getTypeTag[T: ru.TypeTag](obj: T) = ru.typeTag[T]
  val theType = getTypeTag(l).tpe

  val decls = theType.decls.take(10)
  println("decls:" + decls)

  def personCtrom = {
    val m = ru.runtimeMirror(getClass.getClassLoader)
    val classPerson = ru.typeOf[Person].typeSymbol.asClass
    val cm = m.reflectClass(classPerson)
    val ctor = ru.typeOf[Person].decl(ru.termNames.CONSTRUCTOR).asMethod
    cm.reflectConstructor(ctor)
  }

  def purchaseMirror = {
    val p = Purchase("Jeff Lebowski", 23819, false)
    val m = ru.runtimeMirror(p.getClass.getClassLoader)
    val shippingTermSymb = ru.typeOf[Purchase].decl(ru.TermName("shipped")).asTerm
    val im = m.reflect(p)
    im.reflectField(shippingTermSymb)
  }

  val p = Purchase("Jeff Lebowski", 23819, false)
  val m = ru.runtimeMirror(p.getClass.getClassLoader)
  val shippingTermSymb = ru.typeOf[Purchase].decl(ru.TermName("shipped")).asTerm
  val im = m.reflect(p)
  val shippingFieldMirror = im.reflectField(shippingTermSymb)
  shippingFieldMirror.get
  shippingFieldMirror.set(true)


  val c = new C { type T = String }
  val d = new D { type T = String }

  def m[T: ru.TypeTag, S: ru.TypeTag](x: T, y: S): Boolean = {
    val leftTag = ru.typeTag[T]
    val rightTag = ru.typeTag[S]
    leftTag.tpe <:< rightTag.tpe
  }


  def main(args: Array[String]): Unit ={
    println(personCtrom("Mike"))
    val pm = purchaseMirror
    println("pm1:" + pm.get)
    pm.set(true)
    println("pm2:" + pm.get)
    println("pm3:" + purchaseMirror.get)

    println("c is assignable from d: " + c.getClass.isAssignableFrom(d.getClass))
    println("d is extend c: " + m(d, c))
    println("c is extend c: " + m(c, c))
  }
}
