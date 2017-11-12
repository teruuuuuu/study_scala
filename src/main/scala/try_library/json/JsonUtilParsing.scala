package try_library.json

import scala.util.parsing.json.JSON


object JsonUtilParsing {

  def main(args:Array[String]): Unit ={

    val json1 = JSON.parseFull("""
      {"jsByCaseList":[{"name":"red","red":255,"green":0,"blue":0},{"name":"gray","red":100,"green":100,"blue":100}]}
      """)
    println("study.parsing start: Map[String, List[Map[String, String]]]")
    printParsing(json1)
    getParsingByKey(json1)

    val json2 = JSON.parseFull("""
      [{"name":"red","red":255,"green":0,"blue":0},{"name":"gray","red":100,"green":100,"blue":100}]
      """)
    println("study.parsing start: List[Map[String, String]]")
    printParsing(json2)
    getParsingByKey(json2)
  }

  def printParsing(json: Option[Any]): Unit ={
    json match {
      case Some(e:Map[String,List[Map[String, String]]]) => {
        e.foreach { pair =>
          pair._2.foreach{ pairc =>
            pairc.keys.foreach{
              key=>
                println(key + ":" + pairc.get(key))
            }
          }
        }
      }
      case Some(e: List[Map[String, String]]) => {
        e.foreach(
          m =>
            m.keys.foreach{
              key =>
                println(key + ":" + m.get(key))
            }
        )
      }
      case Some(e:Map[String,String]) => {
        e.foreach { pair =>
          println(pair._1 + ":" + pair._2)
        }
      }
      case Some(e)
      =>
        println(e)
      case None => println("Failed.")
    }
  }

  def getParsingByKey(json: Option[Any]): List[String] ={
    json match {
      case Some(e:Map[String,List[Map[String, String]]]) => {
        var ret:List[String] = List()
        e.foreach { pair =>
          pair._2.foreach { pairc =>
            ret = ret :+ pairc.get("name").getOrElse().toString
          }
        }
        ret
      }
      case Some(e: List[Map[String, String]]) => {
        val ret = for(
          m <- e
        ) yield m.get("name").getOrElse().toString
        ret.toList
      }
      case Some(e:Map[String,String]) => {
        List("")
      }
      case Some(e) => {
        List("")
      }
      case None => {
        List("")
      }
    }
  }
}
