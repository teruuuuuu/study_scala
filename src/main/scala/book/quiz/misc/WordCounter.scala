package book.quiz.misc

/**
  * ワードをカウントするオブジェクト。
  */
object WordCounter {

  /**
    * 文字列から単語数をカウントする。
    *
    * @param words 文字列
    * @return 単語がキー、単語数がヴァリューのマップ
    */
  def countWords(words: List[String]): Map[String, Int] = {
    val tempList =
      for(
        word <- words;
        fruit <- word split " "
      ) yield fruit
    tempList.foldLeft(Map[String, Int]().withDefaultValue(0)) {(map, key) => map + (key -> (map(key) + 1))}
  }

  def countWords2(words: List[String]): Map[String, Int] = {
    val tempList =
      for(
        word <- words;
        fruit <- word split " "
      ) yield fruit
    tempList.foldLeft(Map[String, Int]().withDefaultValue(0)) {(map, key) => map + (key -> (map(key) + 1))}
  }

}

object WordCountTest {

  def main(args: Array[String]): Unit = {

    val words = List("apple banana", "orange apple mango", "kiwi papaya orange", "mango orange muscat apple")

    val keywords = Map("foo" -> "bar", "baz" -> "qux")
    var text = "foo is baz."
    keywords.foreach { map =>
      text = text.replace( map._1, map._2)
    }
    println(text)

    val text2 = "foo is baz."
    val changedText2 = keywords.foldLeft(text2){ (tex, map) => tex.replace(map._1, map._2)  }
    println(changedText2)

    println(WordCounter.countWords(words))


    val countWordsList = List("a", "b", "c", "a", "d", "b", "e", "b")
    val wordAggregate = countWordsList.foldLeft(Map[String, Int]().withDefaultValue(0)){
      (map, key) => map + (key -> (map(key) + 1))
    }.toSeq.sortBy(_._2).reverse
    println(wordAggregate)

    val beforeText = "{$1} is better than {$2}"
    val replaceMap = Map("{$1}" -> "health", "{$2}" -> "wealth")
    val afterText = replaceMap.foldLeft(beforeText){ (tex, map) => tex.replace(map._1, map._2) }
    println(afterText)
  }
}