package try_library.json.j4s

import org.json4s.JArray
import org.json4s.jackson.JsonMethods

import scala.collection.immutable.Map
/**
  * パラメータ読み込み後でbeanの各項目の値をlistで保持する
  * @param sinceStr 設定が有効になる開始期間
  * @param configMap
  */
case class JsonPlainConfig(sinceStr: String, configMap: Map[String, Map[String, List[String]]])
/**
  * 同期間内の設定を展開しconfigListに格納します。
  * @param sinceStr 設定が有効になる開始期間
  * @param configList List[ Map[String(bean名),  Map[String(beanの各項目名), String(beanの各項目の値)
  */
case class JsonSameTermConfig(sinceStr: String, configList: List[Map[String, Map[String, String]]])
/**
  * 一期間のjsonパラメータの設定情報を保持します。
  * 期間での組み合わせの情報はList[JsonUseConfig]の形で保持し、期間ごとの組み合わせのパターンはList[List[JsonUseConfig]]で保持します。
  * @param sinceDayStr 設定が有効になる開始曜日
  * @param sinceTimeStr 設定が有効になる開始時間
  * @param confMap Map[String(bean名),  Map[String(beanの各項目名), String(beanの各項目の値)
  */
case class JsonTermConfig(sinceDayStr: String, sinceTimeStr: String, confMap: Map[String, Map[String, String]])
private object J4sSampleBack {
  def loadJsonParam(source: String) = {
    val parseResult = parseToClass(source)
    val expressionResult =sameTermExpression(parseResult)
    termExpression(expressionResult)
  }
  private def parseToClass (source: String) = {
    val parseResult: JArray = try{
      JsonMethods.parse(source).asInstanceOf[JArray]
    } catch {
      case e:Exception => throw J4sParseError.paramLoadException("json4sでパラメータをパースする際にエラーが発生しました。", e)
    }
    //val parseResult: JArray = JsonMethods.parse(source).asInstanceOf[JArray]
    parseResult.values.map{row =>
      row match {
        case s: Map[String, Any] => {
          if(!s.contains("since") || !s.contains("config")){
            // sinceとconfigがjsonパラメータに含まれていない場合はエラーを投げる
            throw throw J4sParseError.invalidJsonParameterByFormat(row, source)
          }
          val sinceStr = s.get("since").getOrElse("-1").toString
          val configMap = s.get("config") match {
            case s: Some[Map[String, Map[String, Any]]] => s.get
            case _ => Map().asInstanceOf[Map[String, Map[String, Any]]]
          }
          try {
            JsonPlainConfig(sinceStr, configMap.transform(strToListVal))
          }catch {
            case e:Exception => throw J4sParseError.invalidJsonParameterByFormat(row, source)
          }
        }
        case _ => throw J4sParseError.invalidJsonParameterByFormat(row, source)
      }
    }
  }
  private def strToListVal = (key: String, conf: Any) => {
    conf match {
      case m: Map[String, Map[String, Option[Any]]] =>
        var  ret = Map().asInstanceOf[Map[String, List[String]]]
        m.keys.foreach{ key =>
          val mVal = m.get(key) match {
            case x: Option[Any] => x.get
            case x => x
          }
          val keyVal = mVal match {
            case l: List[Any] => {
              var li = List().asInstanceOf[List[String]]
              var xList = l
              for (i <- 0 to xList.size - 1) {
                li :+= xList.head.toString
                xList = xList.tail
              }
              li
            }
            case s: Any =>  List(s.toString)
          }
          ret = ret ++ Map(key -> keyVal)
        }
        ret
      case _ =>
        throw new Exception
    }
  }
  /**
    *  同一期間内で展開されたパラメータの情報を返します。
    * @param parseResult　各beanの項目ごとの値をListで保持した展開前の情報
    * @return　各beanの項目ごとの値をStringで保持するように同一期間で展開した情報
    */
  private def sameTermExpression(parseResult: List[JsonPlainConfig]): List[JsonSameTermConfig] = {
    // 戻り値のvarを初期化
    var returnVar = List.empty[JsonSameTermConfig]
    parseResult.foreach{spanConfig => // 各期間ごとの設定
      // 同一期間内の設定を保持するリストを初期化
      var termConfigList = List.empty[Map[String, Map[String, String]]]
      val beanNames = spanConfig.configMap.keys
      beanNames.foreach{ beanName =>
        spanConfig.configMap.get(beanName).get.foreach{ beanCol => // 期間ごとのbeanの各項目ごとの値で処理を実施
          // beanの項目の値を取得
          val mapList: List[Map[String, String]] = beanCol._2.map{ v =>
            Map(beanCol._1 -> v)
          }
          // varの変数に値を逐一追加し、termConfigListを更新する
          var newConfigList = List.empty[Map[String, Map[String, String]]]
          mapList.foreach{ mapV =>
            if(termConfigList.size == 0){
              // 期間内での最初の設定
              newConfigList :+= Map(beanName -> mapV)
            }else{
              // 期間内での設定が既にある場合は前回の結果に追加する
              termConfigList.foreach{ confMap =>
                confMap match {
                  case x: Map[String, Map[String, String]] =>
                    if(x.contains(beanName)){
                      // 期間内での設定に当該のbeanが既に含まれている場合は、取り出してから更新したものを新しいリストに追加する
                      val beanMap = confMap.get(beanName).get
                      val newBeanMap = beanMap ++ mapV
                      val newConfMap = confMap updated (beanName, newBeanMap)
                      newConfigList :+= newConfMap
                    }else{
                      // 期間内での設定に当該のbeanがまだない場合は、当該のbeaを含めたマップを新規に作成し新しいリストに追加する
                      val newConfMap = confMap ++ Map(beanName -> mapV)
                      newConfigList :+= newConfMap
                    }
                }
              }
            }
          }
          // 同一期間内での一つのbeanの展開が終了したので、mConfigListに反映しnewConfigListを初期化する
          termConfigList = newConfigList
          newConfigList = List.empty[Map[String, Map[String, String]]]
        }
      }
      // 同一期間内でのすべてのbeanの展開が終了したので結果を戻り値に追加
      returnVar :+= JsonSameTermConfig(spanConfig.sinceStr, termConfigList)
    }
    returnVar
  }
  /**
    *
    * @param expressionResult 同一期間内で各beanの項目ごとの値が展開された情報
    * @return　List[List[JsonTermConfig]]　期間ごとの組み合わせのパターンを返す。期間ごとの組み合わせはList[JsonTermConfig]で保持する
    */
  private def termExpression(expressionResult: List[JsonSameTermConfig]): List[List[JsonTermConfig]] = {
    // 戻り値を初期化
    var returnVar = List.empty[List[JsonTermConfig]]
    expressionResult foreach { spanConfig => // 期間ごとの設定で処理を実施
      val since = spanConfig.sinceStr.split("/")
      since match {
        case Array(sinceDay, sinceTime) => {
          var newResult = List.empty[List[JsonTermConfig]]
          if(returnVar.isEmpty){
            // 戻り値のリストが空（最初の期間）の場合は、各設定をリストに変換して戻り値のリストに追加する
            if(spanConfig.configList.isEmpty) {
              // configが空の場合はsinceだけ設定された空の設定を追加
              newResult = List(JsonTermConfig(sinceDay, sinceTime, Map())) :: newResult
            }else {
              spanConfig.configList foreach{ config =>
                JsonTermConfig(sinceDay, sinceTime, config) match {
                  case x: JsonTermConfig =>
                    newResult = List(x) :: newResult
                }
              }
            }
          }else{
            // 戻り値のリストが空でない場合は、既存のリストに要素を追加したものを新しいリストに追加し、戻り値のリストを更新する
            if(spanConfig.configList.isEmpty) {
              // sinceだけ設定があり、configが空の場合はsinceだけ値が入っているものを新しいリストに追加する
              returnVar.foreach { ret =>
                newResult = (ret :+ JsonTermConfig(sinceDay, sinceTime, Map())) :: newResult
              }
            }else{
              spanConfig.configList foreach { config =>
                returnVar.foreach { ret =>
                  newResult = (ret :+ JsonTermConfig(sinceDay, sinceTime, config)) :: newResult
                }
              }
            }
          }
          // 期間内での展開が終了したので、戻り値を更新する
          // 先頭への要素への追加を行っていたので、reverseで順番を戻す
          returnVar = newResult.reverse
        }
        case _ =>
          // sinceが"d/hh:mm:ss"のフォーマットでない場合はエラーを投げる
          throw J4sParseError.invalidJsonParameterBySinceFormat(spanConfig.sinceStr, expressionResult.toString)
      }
    }
    returnVar
  }
}