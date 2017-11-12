package try_library.json

import spray.json.{DefaultJsonProtocol, DeserializationException, JsNumber, JsObject, JsString, JsValue, RootJsonFormat}


trait ColorToJsonTrait extends DefaultJsonProtocol{
  class Color(val name: String, val red: Int, val green: Int, val blue: Int)

  implicit object ColorJsonFormat extends RootJsonFormat[Color] {
    def write(c: Color) = JsObject(
      "name" -> JsString(c.name),
      "red" -> JsNumber(c.red),
      "green" -> JsNumber(c.green),
      "blue" -> JsNumber(c.blue)
    )

    def read(value: JsValue) = {
      value.asJsObject.getFields("name", "red", "green", "blue") match {
        case Seq(JsString(name), JsNumber(red), JsNumber(green), JsNumber(blue)) =>
          new Color(name, red.toInt, green.toInt, blue.toInt)
        case _ => throw new DeserializationException("Color expected")
      }
    }
  }
}


object SprayJsonSample extends ColorToJsonTrait {
  case class TestJson(name: String, red: Int, green: Int, blue: Int)

  def main(args: Array[String]): Unit = {
    val colorList: List[Color] = List(
      new Color("red", 255, 0, 0),
      new Color("gray", 100, 100, 100)
    )

    val colorListJson: JsValue = colorList.toJson
    println(colorListJson)

    val clorJsonToList: List[Color] = colorListJson.convertTo[List[Color]]
    clorJsonToList.foreach { color =>
      println(color.name)
    }
  }
}


trait JsonByCaseTrait extends DefaultJsonProtocol{
  case class JsonByCase(name: String, red: Int, green: Int, blue: Int)
  implicit val testJsonFormat = jsonFormat4(JsonByCase.apply)

  case class JsonByCaseList(jsByCaseList: List[JsonByCase])
  implicit val testJsonListFormat = jsonFormat1(JsonByCaseList.apply)

  case class JsonByCase2(name: String, json: List[JsonByCase])
  implicit val testJsonFormat2 = jsonFormat2(JsonByCase2.apply)
}


object SpreySampleByCase extends JsonByCaseTrait {

  def main(args:Array[String]): Unit ={
    println("case1")
    val testJsonList: List[JsonByCase] = List(
      new JsonByCase("red", 255, 0, 0),
      new JsonByCase("gray", 100, 100, 100)
    )

    val testJson: JsValue = testJsonList.toJson
    println(testJson)
    val testJsonListFromJsVal: List[JsonByCase] = testJson.convertTo[List[JsonByCase]]
    testJsonListFromJsVal.foreach{ jsByCase =>
      println(jsByCase.name)
    }

    println("case2")
    val testJsonList2: List[JsonByCase2] = List(
      new JsonByCase2("color list", testJsonList)
    )
    val testJson2: JsValue = testJsonList2.toJson
    println(testJson2)
    val testJsonListFromJsVal2: List[JsonByCase2] = testJson2.convertTo[List[JsonByCase2]]
    testJsonListFromJsVal2.foreach{ jsByCase =>
      println(jsByCase.name)
    }
  }
}
