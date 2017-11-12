package book.scalaScalableProgramming.chap10

import book.scalaScalableProgramming.chap10.Element.elem

object Element{
  private class ArrayElement(val contents: Array[String]) extends Element
  private class LineElement(s: String) extends Element{
    val contents = Array(s)
    override def width = s.length
    override def height = 1
  }
  private class UniformElement(
    ch:Char,
    override val width: Int,
    override val height:Int
  )extends Element{
    private val line = ch.toString * width
    def contents = Array.fill(height)(line)
  }
  def elem(contents:Array[String]):Element =
    new ArrayElement(contents)
  def elem(chr: Char, width: Int, height: Int ): Element =
    new UniformElement(chr, width, height)
  def elem(line: String): Element =
    new LineElement(line)
}

abstract class Element {
  def contents: Array[String]
  def width: Int = contents(0).length
  def height: Int = contents.length

  def above(that: Element): Element = {
    val this1 = this widen that.width
    val that1 = that widen this.width
    assert(this1.width == that1.width)
    elem(this1.contents ++ that1.contents)
  }

  def beside(that: Element): Element = {
    val this1 = this heighten that.height
    val that1 = that heighten this.height
    elem(
      for((line1, line2) <- this1.contents zip that1.contents)
      yield  line1 + line2)

  }

  def widen(w:Int):Element =
    if(w<=width) this
    else{
      val left = elem(' ', (w-width)/2, height)
      val right = elem(' ', w - width - left.width, height)
      left beside this beside right
    }

  def heighten(h: Int): Element =
    if(h <= height)this
    else{
      val top = elem(' ', width, (h - height)/2)
      val bot = elem(' ', width, h - height - top.height)
      top above this above bot
      /*
      val tmp = elem(' ', width, h - height)
      tmp above this
      */
    }
  override def toString = contents mkString "\n"
}

object Spiral {
  val space = elem(" ")
  val corner = elem("+")

  def spiral(nEdge: Int, direction: Int): Element = {
    if(nEdge == 1)
      elem("+")
    else{
      val sp = spiral(nEdge -1, (direction+3) %4)
      def verticalBar = elem('|', 1, sp.height)
      def horizontalBar = elem('-', sp.width, 1)
      if(direction == 0)
        (corner beside horizontalBar) above (sp beside space)
      else if(direction == 1)
        (sp above space) beside (corner above verticalBar)
      else if(direction == 2)
        (space beside sp) above (horizontalBar beside corner)
      else
        (verticalBar above corner) beside (space above sp)

    }
  }
  def main(args: Array[String]): Unit ={
    val nSides = args.length match {
      case 0 => 3
      case _ => args(0).toInt
    }
    //println(spiral(nSides, 0))
    /*
    val sp = elem("+")
    def verticalBar = elem('|', 1, sp.height)
    def horizontalBar = elem('-', sp.width, 1)
    println(corner beside horizontalBar)
    */
    val firstLine = elem("+")
    println(firstLine)
    println("\n")

    val verticalBar1 = elem('|', 1, firstLine.height)
    val horizontalBar1 = elem('-', firstLine.width, 1)
    val secondLine = (space beside firstLine) above (horizontalBar1 beside corner)
    println(secondLine)
    println("\n")

    val verticalBar2 = elem('|', 1, secondLine.height)
    val horizontalBar2 = elem('-', secondLine.width, 1)
    val thirdLine = (verticalBar2 above corner) beside (space above secondLine)
    println(thirdLine)
    println("\n")

    val verticalBar3 = elem('|', 1, thirdLine.height)
    val horizontalBar3 = elem('-', thirdLine.width, 1)
    val fourthLine = (corner beside horizontalBar3) above (thirdLine beside space)
    println(fourthLine)
    println("\n")

    val verticalBar4 = elem('|', 1, fourthLine.height)
    val horizontalBar4 = elem('-', fourthLine.width, 1)
    val fifthLine = (fourthLine above space) beside (corner above verticalBar4)
    println(fifthLine)
    println("\n")

    val verticalBar5 = elem('|', 1, fifthLine.height)
    val horizontalBar5 = elem('-', fifthLine.width, 1)
    val sixthLine = (space beside fifthLine) above (horizontalBar5 beside corner)
    println(sixthLine)
    println("\n")

    val verticalBar6 = elem('|', 1, sixthLine.height)
    val horizontalBar6 = elem('-', sixthLine.width, 1)
    val seventhLine = (verticalBar6 above corner) beside (space above sixthLine)
    println(seventhLine)
    println("\n")
  }

}
