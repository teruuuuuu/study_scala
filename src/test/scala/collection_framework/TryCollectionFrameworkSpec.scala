package collection_framework

import org.scalatest.FlatSpec
import scala.collection.mutable.ArrayBuffer

import collection_framework.TryCollectionFramework._

class TryCollectionFrameworkSpec extends FlatSpec {

  "foldLeft" should "count collectry" in {

    val countResult = keyCounts(List("a", "b", "c", "d", "a", "b", "a", "e"))
    val answer = ArrayBuffer(("a",3), ("b",2), ("d",1), ("c",1), ("e",1))

    assert(countResult == answer)
  }

  "expand" should "expand collectry1" in {

    val testData = Map("a" -> List("b","c","d","e","f","g"))
    val result = expand(testData)

    val answer = List(Map("a" -> "b"), Map("a" -> "c"), Map("a" -> "d"), Map("a" -> "e"), Map("a" -> "f"), Map("a" -> "g"))
    assert(result == answer)
  }

  "try" should "flatmap" in {

    val testData = Map("a" -> List("b","c","d","e","f","g"), "A" -> List("B","C","D","E","F","G"))
    val answer1 = List("a","b","c","d","e","f","g", "A" ,"B","C","D","E","F","G")
    val result = testData.flatMap {v => v._1 +: v._2 }
    assert(result == answer1)

    val result2 = testData.map(v => v._1 +: v._2)
    val answer2 = List(List("a","b","c","d","e","f","g"), List("A" ,"B","C","D","E","F","G"))
    assert(result2 == answer2)
  }

  "try" should "foldLeft" in {
    val testData = Map("a" -> List("b","c","d","e","f","g"), "A" -> List("B","C","D","E","F","G"))

    val ret = testData.foldLeft(""){
      (acc, v) => acc + v._1 + v._2.foldLeft(""){(acc, v) => acc + v}
    }
    println(ret)
  }

  "try" should "map values" in {
    val testData = Map(1 -> "a", 2 -> "b", 3 -> "c")
    val result = testData.mapValues (_ + "d")

    val answer = Map(1 -> "ad", 2 -> "bd", 3 -> "cd")
    assert(result == answer)

    val testData2 = Map(1 -> List("a", "b", "c", "d", "a", "b", "a", "e"), 2 -> List("B","C","D","E","F","G"))

    val a = testData2.mapValues{v=>
      v.foldLeft(""){(acc, v) => acc + v}
    }

    println(a)
  }
}
