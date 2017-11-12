package study.myParser

import study.myParser.Element.elem

/**
  * Created by arimuraterutoshi on 2017/02/05.
  */
object SpiralExpress {
  val space = elem(" ")
  val corner = elem("+")

  def main(args: Array[String]): Unit ={
    val nSides = args.length match {
      case 0 => 3
      case _ => args(0).toInt
    }
    //println(spiral(nSides, 0))
    val firstLine = elem("+")
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
