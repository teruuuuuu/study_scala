package collection_framework

import try_library.json.j4s.JsonSameTermConfig


case class TryCollectionSampleCase1(a: String, b: Map[String, Map[String, List[String]]])
case class TryCollectionSampleCase2(a: String, b: List[Map[String, Map[String, String]]])

object TryCollectionFramework {

  def keyCounts(countWordsList: List[String]) = {
    countWordsList.foldLeft(Map[String, Int]().withDefaultValue(0)){
      (map, key) => map + (key -> (map(key) + 1))
    }.toSeq.sortBy(_._2).reverse
  }

  def expand[String, V](m: Map[String, List[V]]): List[Map[String, V]] =
    m.foldLeft(List(Map.empty[String, V])){
      case (acc, (k, l)) => l.flatMap(v => acc.map(_ + ((k, v))))
    }


  def sameTermExpression(case1: Seq[TryCollectionSampleCase1]): Seq[JsonSameTermConfig] = {
    case1.map{spanConfig => // 各期間ごとの設定
      // 同一期間内でのbean内の各項目展開後、beanごとの組み合わせで展開する
      JsonSameTermConfig(spanConfig.a, expand(spanConfig.b.mapValues(expand)))
    }
  }
}
