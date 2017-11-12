package study.parsing

import scala.util.parsing.json.JSON

/**
  * Created by arimuraterutoshi on 2017/02/12.
  */
object JsonParsingSample {

  def main(args: Array[String]): Unit ={
    sample1
    sample2
    sample3

  }

  def sample1: Unit = {
    val haveFoo: Option[String] = Option("foo")
    val nestedFooBar: Option[ Option[String]] = Option(haveFoo)
    val message1: String = (for{
      haveFoo <- nestedFooBar
      text    <- haveFoo
    } yield {
      text
    }).getOrElse("なにも入っていません")

    println(message1)
  }

  def sample2: Unit = {
    val result = JSON.parseFull("""
      {"name": "Naoki",  "lang": ["Java", "Scala"]}
    """)

    result match {
      case Some(e) => println(e) // => Map(name -> Naoki, lang -> List(Java, Scala))
      case None => println("Failed.")
    }
  }

  def sample3: Unit ={
    val json = JSON.parseFull("""
      {"jsByCaseList":[{"name":"red","red":255,"green":0,"blue":0},{"name":"gray","red":100,"green":100,"blue":100}]}
      """)

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
      case Some(e:Map[String,String]) => {
        println(e);
        e.foreach { pair =>
          println(pair._1 + ":" + pair._2)
        }
      }
      case Some(e) => println(e)
      case None => println("Failed.")
    }
  }
}
