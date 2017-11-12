package book.codeIq.challenge3118

import book.codeIq.challenge3118.Main

import book.quiz.misc.WordCounter
import org.scalatest.{FunSpec, Matchers}

class RouteSearchTest extends FunSpec with Matchers {
  describe("CodeIQ#search shortest route") {
    it("shortest distance") {
      val mainArgs: Array[String] = Array()
      val routeInfo = Array(Array(5,6,7), Array(1,3,3), Array(5,0,2))
      //println(Main.searchShortestDistance(routeInfo))
      println(Main.searchShortestDistance2(routeInfo))
      Main.dummy shouldBe true
    }
  }
}
