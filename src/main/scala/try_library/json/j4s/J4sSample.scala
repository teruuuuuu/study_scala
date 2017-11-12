package try_library.json.j4s

import java.time.{DayOfWeek, LocalTime}

import org.json4s.JArray
import org.json4s.jackson.JsonMethods


/**
  * 一期間のjsonパラメータの設定情報を保持します。
  * 期間での組み合わせの情報はList[JsonUseConfig]の形で保持し、期間ごとの組み合わせのパターンはList[List[JsonUseConfig]]で保持します。
  * @param sinceStr 設定が有効になる開始期間
  * @param confMap Map[String(bean名),  Map[String(beanの各項目名), String(beanの各項目の値)
  */
case class JsonTermConfigTemp(sinceStr: String, confMap: Map[String, Map[String, String]])


case class JsonConfig(dayOfWeek: DayOfWeek, localTime: LocalTime, confMap: Map[String, Map[String, String]])

private object JsonParamParser {

  /**
    * jsonの文字列をパースして型にセットするインターフェースのメソッド
    * @param source
    * @return
    */
  def loadJsonParam(source: String) = {
    val parseResult = parseToClass(source)
    val expressionResult =sameTermExpression(parseResult)
    val termExpressionResult = termExpression(expressionResult)
    timeFormatCheck(termExpressionResult)
  }

  /**
    * json4sを実行した結果をList[JsonPlainConfig]に変換する
    * JsonPlainConfigは期間内での各beanの各項目の値をリストで保持する
    * @param source
    * @return
    */
  private def parseToClass (source: String) = {
    val j4sParseResult: JArray = try{
      JsonMethods.parse(source).asInstanceOf[JArray]
    } catch {
      case e:Exception => throw J4sParseError.paramLoadException("json4sでパラメータをパースする際にエラーが発生しました。", e)
    }
    j4sParseResult.values.map{j4sRow =>
      val j4sRowMap = j4sRow.asInstanceOf[Map[String, Any]]
      val sinceStr = j4sRowMap.get("since").getOrElse(throw J4sParseError.invalidJsonParameterByFormat(j4sRow, source)).toString
      val configMap = j4sRowMap.get("config").getOrElse(throw J4sParseError.invalidJsonParameterByFormat(j4sRow, source)) match {
        case s: Map[String, Map[String, Any]] => s
        case _ => throw J4sParseError.invalidJsonParameterByFormat(j4sRow, source)
      }
      try {
        // configの値をlistに変換する
        JsonPlainConfig(sinceStr, configMap.transform(anyToListVal))
      }catch {
        case e:Exception => throw J4sParseError.invalidJsonParameterByFormat(j4sRow, source)
      }
    }
  }

  /**
    *  beanの各項目の値をリストで保持する形(List[JsonPlainConfig])から
    *  同一期間内でbeanの各項目を展開し、組み合わせごとの情報を持てるようにSeq[JsonSameTermConfig]型に変換する。
    * @param parseResult　各beanの項目ごとの値をListで保持した展開前の情報
    * @return　各beanの項目ごとの値をStringで保持するように同一期間で展開した情報
    */
  private def sameTermExpression(parseResult: Seq[JsonPlainConfig]): Seq[JsonSameTermConfig] = {
    parseResult.map{spanConfig => // 各期間ごとの設定
      // 同一期間内でのbean内の各項目展開後、beanごとの組み合わせで展開する
      JsonSameTermConfig(spanConfig.sinceStr, expand(spanConfig.configMap.mapValues(expand)))
    }
  }

  /**
    * 同一期間でのbeanの各項目が展開された情報(Seq[JsonSameTermConfig])を受取り、
    * 期間ごとの設定を組み合わせた(Seq[Seq[JsonTermConfigTemp]])を返す。
    * 一番外側のリストが週毎の情報を表し、一つ内側のリストは週間の設定を表す。
    * @param expressionResult 同一期間内で各beanの項目ごとの値が展開された情報
    * @return　List[List[JsonTermConfig]]　期間ごとの組み合わせのパターンを返す。期間ごとの組み合わせはList[JsonTermConfig]で保持する
    */
  private def termExpression(expressionResult: Seq[JsonSameTermConfig]): Seq[Seq[JsonTermConfigTemp]] = {
    expressionResult.map(m =>
      m.configList.map(JsonTermConfigTemp(m.sinceStr, _))).reverse
      .foldRight(List(List.empty[JsonTermConfigTemp])) {
        case (d, acc) => d.flatMap { v =>acc.map(v :: _)}
      }.map(_.reverse)
    //    expressionResult.foldLeft(List(List.empty[JsonTermConfigTemp])) {
    //      case (acc, m) => {
    //        m.configList.map(JsonTermConfigTemp(m.sinceStr, _)).flatMap { v => acc.map(_ :+ v) }
    //      }
    //    }

//    expressionResult.map(m => m.configList.map(JsonTermConfigTemp(m.sinceStr, _)))
//      .foldLeft(List(List.empty[JsonTermConfigTemp])){
//        case (acc, m) => m.flatMap{v=> acc.map(_ :+ v)}
//        }
  }

  /**
    * 日付のフォーマットをチェックして、DayOfWeek型とLocalTime型に変換する
    * @param termList
    * @return
    */
  private def timeFormatCheck( termList: Seq[Seq[JsonTermConfigTemp]]) = {
    termList.map(_.map{row =>
      row.sinceStr.split("/") match {
        case Array(sinceDay, sinceTime) => {
          try{
            val week = DayOfWeek.of(sinceDay.toInt)
            val time = LocalTime.parse(sinceTime)
            JsonConfig(week, time, row.confMap)
          } catch {
            case e:Exception =>
              throw J4sParseError.paramLoadException("日付のフォーマットチェック中にエラーが発生しました", e)
          }
        }
        case _ =>
          throw J4sParseError.invalidJsonParameterBySinceFormat(row.sinceStr, row.toString)
      }
    })
  }

  /**
    * map内にあるリストを外に出す
    * @param m
    * @tparam String
    * @tparam V
    * @return
    */
  private def expand[String, V](m: Map[String, List[V]]): List[Map[String, V]] =
    m.foldLeft(List(Map.empty[String, V])){case (acc, (k, l)) => l.flatMap(v => acc.map(_ + ((k, v))))}

  /**
    * json4sの結果に対してbeanの各項目の値をリストで持てるように変換する
    * @return
    */
  private def anyToListVal = (key: String, beanMap: Map[String, Any]) => {

    beanMap.flatMap{ bean => bean._2 match {
      case colListVal: List[Any] => Map(bean._1 -> colListVal.map(_.toString))
      case s: Any => Map(bean._1 -> List(s.toString))
    }}
  }
}