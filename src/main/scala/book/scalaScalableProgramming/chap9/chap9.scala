package book.scalaScalableProgramming.chap9

import java.io.{File, PrintWriter}
import javafx.application.Application

import scala.io.Source


trait myPrintT {
  def withPrintWriter(file: File)(op: PrintWriter => Unit): Unit = {
    val writer = new PrintWriter(file)
    try{
      op(writer)
    }finally{
      writer.close()
    }
  }
}

object myPrint extends  myPrintT{
  def print(fileName:String) = {
    val file = new File(fileName)
    println(new File(".").getAbsoluteFile().getParent())
    withPrintWriter(file){
      writer => writer.println( new java.util.Date)
    }
  }


  def main(args: Array[String]): Unit ={
    print("./src/main/scala/book.scalaScalableProgramming/chap9/date.txt")
  }
}

object fileLength{
  def palinPrint(fileName: String): Unit ={
    val lines = Source.fromFile(fileName).getLines().toList
    val result = lineWithNum(lines, widthOfLength(longestLine(lines))) {
      line => true
    }
    print(result.mkString)
  }

  def grepPrint(fileName: String, text: String): Unit ={
    val lines = Source.fromFile(fileName).getLines().toList
    val result = lineWithNum(lines, widthOfLength(longestLine(lines))) {
      line => line.contains(text)
    }
    print(result.mkString)
  }


  def widthOfLength(s: String) = s.length.toString.length
  def longestLine(lines:List[String]) = lines.reduceLeft(
    (a, b) => if(a.length > b.length) a else b
  )
  def lineWithNum(lines:List[String], maxWidth: Int)(op:Function[String, Boolean]) ={
    for{
      line <- lines
      if op(line)
    } yield " " * (maxWidth - widthOfLength(line)) + line.length + "|" + line + "\n"
  }

  def main(args: Array[String]): Unit ={
    val fileName = args.length > 0 match{
      case true => args(0)
      case _ => "./src/main/scala/book.scalaScalableProgramming/chap9/chap9.scala"
    }
    //palinPrint(fileName)
    grepPrint(fileName, "def")
  }
}