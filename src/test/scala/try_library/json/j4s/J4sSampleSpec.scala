package try_library.json.j4s

import java.time.{DayOfWeek, LocalTime}

import org.scalatest.FlatSpec
import try_library.json.j4s.J4sParseError.{JsonParameterInvalid, ParameterLoaderException}

import scala.collection.immutable.Map

class J4sSampleSpec extends  FlatSpec {

  /**
    * リストなしの状態で確認動作することを確認
    */
  "jsonParamParser" should "parse correctly1" in {
    val source =
      """
          [
            {
              "since": "1/00:00:00",
              "config": {
                "customerPricingConfig": {
                  "ktyrIsEnabled": 0,
                  "yoseIsEnabled": 1
                },
                "bestPricingConfig": {
                   "abortPoint": 2
                 }
              }
            },
            {
              "since": "3/08:30:00",
              "config": {
                "customerPricingConfig": {
                   "ktyrIsEnabled": 3,
                   "yoseIsEnabled": 4
                }
              }
            },
            {
              "since": "5/06:00:00",
              "config": {
              }
            }
          ]
        """
    val parseResult = JsonParamParser.loadJsonParam(source)

    val answer = List(
      List(
        JsonConfig(DayOfWeek.MONDAY, LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "1"), "bestPricingConfig" -> Map("abortPoint" -> "2"))),
        JsonConfig(DayOfWeek.WEDNESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "3", "yoseIsEnabled" -> "4"))),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map())
      )
    )
    assert(parseResult == answer)
  }

  /**
    * 一つの期間でリストありの状態で確認
    */
  "jsonParamParser" should "parse correctly2" in {
    val source =
      """
          [
            {
              "since": "1/00:00:00",
              "config": {
                "customerPricingConfig": {
                  "ktyrIsEnabled": [0,1],
                  "yoseIsEnabled": 1
                },
                "bestPricingConfig": {
                   "abortPoint": 2
                 }
              }
            },
            {
              "since": "3/08:30:00",
              "config": {
                "customerPricingConfig": {
                   "ktyrIsEnabled": 3,
                   "yoseIsEnabled": 4
                }
              }
            },
            {
              "since": "5/06:00:00",
              "config": {
              }
            }
          ]
        """
    val parseResult = JsonParamParser.loadJsonParam(source)
    val answer = List(
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "1"), "bestPricingConfig" -> Map("abortPoint" -> "2"))),
        JsonConfig(DayOfWeek.WEDNESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "3", "yoseIsEnabled" -> "4"))),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map())
      ),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"), Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "1"), "bestPricingConfig" -> Map("abortPoint" -> "2"))),
        JsonConfig(DayOfWeek.WEDNESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "3", "yoseIsEnabled" -> "4"))),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map())
      )
    )

    assert(parseResult == answer)
  }

  /**
    * 複数の期間でリストありの状態で確認
    */
  "jsonParamParser" should "parse correctly3" in {
    val source =
      """
          [
            {
              "since": "1/00:00:00",
              "config": {
                "customerPricingConfig": {
                  "ktyrIsEnabled": [0,1],
                  "yoseIsEnabled":  [0,1]
                },
                "bestPricingConfig": {
                   "abortPoint": [123456, 7890]
                 }
              }
            },
            {
              "since": "3/08:30:00",
              "config": {
                "customerPricingConfig": {
                   "ktyrIsEnabled":  [0,1],
                   "yoseIsEnabled": [  0,   "1"]
                }
              }
            },
            {
              "since": "5/06:00:00",
              "config": {
              }
            }
          ]
        """
    val parseResult = JsonParamParser.loadJsonParam(source)
    val answer = List(
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "0"), "bestPricingConfig" -> Map("abortPoint" -> "123456"))),
        JsonConfig(DayOfWeek.WEDNESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "0"))),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map())
      ),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "0"), "bestPricingConfig" -> Map("abortPoint" -> "123456"))),
        JsonConfig(DayOfWeek.WEDNESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "0"))),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map())
      ),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "1"), "bestPricingConfig" -> Map("abortPoint" -> "123456"))),
        JsonConfig(DayOfWeek.WEDNESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "0"))),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map())
      ),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "1"), "bestPricingConfig" -> Map("abortPoint" -> "123456"))),
        JsonConfig(DayOfWeek.WEDNESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "0"))),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map())
      ),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "0"), "bestPricingConfig" -> Map("abortPoint" -> "7890"))),
        JsonConfig(DayOfWeek.WEDNESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "0"))),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map())
      ),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "0"), "bestPricingConfig" -> Map("abortPoint" -> "7890"))),
        JsonConfig(DayOfWeek.WEDNESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "0"))),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map())
      ),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "1"), "bestPricingConfig" -> Map("abortPoint" -> "7890"))),
        JsonConfig(DayOfWeek.WEDNESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "0"))),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map())
      ),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "1"), "bestPricingConfig" -> Map("abortPoint" -> "7890"))),
        JsonConfig(DayOfWeek.WEDNESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "0"))),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map())
      ),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "0"), "bestPricingConfig" -> Map("abortPoint" -> "123456"))),
        JsonConfig(DayOfWeek.WEDNESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "0"))),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map())
      ),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "0"), "bestPricingConfig" -> Map("abortPoint" -> "123456"))),
        JsonConfig(DayOfWeek.WEDNESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "0"))),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map())
      ),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "1"), "bestPricingConfig" -> Map("abortPoint" -> "123456"))),
        JsonConfig(DayOfWeek.WEDNESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "0"))),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map())
      ),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled"-> "1"), "bestPricingConfig" -> Map("abortPoint" -> "123456"))),
        JsonConfig(DayOfWeek.WEDNESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "0"))),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map())
      ),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "0"), "bestPricingConfig" -> Map("abortPoint" -> "7890"))),
        JsonConfig(DayOfWeek.WEDNESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "0"))),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map())
      ),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "0"), "bestPricingConfig" -> Map("abortPoint" -> "7890"))),
        JsonConfig(DayOfWeek.WEDNESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "0"))),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map())
      ),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "1"), "bestPricingConfig" -> Map("abortPoint" -> "7890"))),
        JsonConfig(DayOfWeek.WEDNESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "0"))),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map())
      ),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "1"), "bestPricingConfig" -> Map("abortPoint" -> "7890"))),
        JsonConfig(DayOfWeek.WEDNESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "0"))),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map())
      ),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "0"), "bestPricingConfig" -> Map("abortPoint" -> "123456"))),
        JsonConfig(DayOfWeek.WEDNESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "1"))),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map())
      ),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "0"), "bestPricingConfig" -> Map("abortPoint" -> "123456"))),
        JsonConfig(DayOfWeek.WEDNESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "1"))),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map())
      ),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "1"), "bestPricingConfig" -> Map("abortPoint" -> "123456"))),
        JsonConfig(DayOfWeek.WEDNESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "1"))),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map())
      ),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "1"), "bestPricingConfig" -> Map("abortPoint" -> "123456"))),
        JsonConfig(DayOfWeek.WEDNESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "1"))),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map())
      ),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "0"), "bestPricingConfig" -> Map("abortPoint" -> "7890"))),
        JsonConfig(DayOfWeek.WEDNESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "1"))),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map())
      ),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "0"), "bestPricingConfig" -> Map("abortPoint" -> "7890"))),
        JsonConfig(DayOfWeek.WEDNESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "1"))),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map())
      ),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "1"), "bestPricingConfig" -> Map("abortPoint" -> "7890"))),
        JsonConfig(DayOfWeek.WEDNESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "1"))),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map())
      ),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "1"), "bestPricingConfig" -> Map("abortPoint" -> "7890"))),
        JsonConfig(DayOfWeek.WEDNESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "1"))),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map())
      ),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "0"), "bestPricingConfig" -> Map("abortPoint" -> "123456"))),
        JsonConfig(DayOfWeek.WEDNESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "1"))),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map())
      ),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "0"), "bestPricingConfig" -> Map("abortPoint" -> "123456"))),
        JsonConfig(DayOfWeek.WEDNESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "1"))),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map())
      ),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "1"), "bestPricingConfig" -> Map("abortPoint" -> "123456"))),
        JsonConfig(DayOfWeek.WEDNESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "1"))),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map())
      ),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "1"), "bestPricingConfig" -> Map("abortPoint" -> "123456"))),
        JsonConfig(DayOfWeek.WEDNESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "1"))),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map())
      ),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "0"), "bestPricingConfig" -> Map("abortPoint" -> "7890"))),
        JsonConfig(DayOfWeek.WEDNESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "1"))),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map())
      ),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "0"), "bestPricingConfig" -> Map("abortPoint" -> "7890"))),
        JsonConfig(DayOfWeek.WEDNESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "1"))),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map())
      ),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "1"), "bestPricingConfig" -> Map("abortPoint" -> "7890"))),
        JsonConfig(DayOfWeek.WEDNESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "1"))),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map())
      ),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "1"), "bestPricingConfig" -> Map("abortPoint" -> "7890"))),
        JsonConfig(DayOfWeek.WEDNESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "1"))),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map())
      )
    )

    assert(parseResult == answer)
  }

  /**
    * 文字列のリストがパラメータに含まれる場合
    */
  "jsonParamParser" should "parse correctly4" in {
    val source =
      """
          [
            {
              "since": "1/00:00:00",
              "config": {
                "customerPricingConfig": {
                  "ktyrIsEnabled": [0,1],
                  "yoseIsEnabled":  [0,1]
                },
                "bestPricingConfig": {
                   "abortPoint": [123456, 7890]
                 }
              }
            },
            {
              "since": "3/08:30:00",
              "config": {
                "customerPricingConfig": {
                   "ktyrIsEnabled":  [0,1],
                   "yoseIsEnabled": [  0,   "1"]
                }
              }
            },
            {
              "since": "5/06:00:00",
              "config": {
                "activeMode": {
                    "priceMode":  ["abc","def"]
                 }
              }
            }
          ]
        """
    val parseResult = JsonParamParser.loadJsonParam(source)
    val answer = List(
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "0"), "bestPricingConfig" -> Map("abortPoint" -> "123456"))),
        JsonConfig(DayOfWeek.WEDNESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "0"))),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map("activeMode" -> Map("priceMode" -> "abc")))),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "0"), "bestPricingConfig" -> Map("abortPoint" -> "123456"))),
        JsonConfig(DayOfWeek.WEDNESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "0"))),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map("activeMode" -> Map("priceMode" -> "abc")))),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "1"), "bestPricingConfig" -> Map("abortPoint" -> "123456"))),
        JsonConfig(DayOfWeek.WEDNESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "0"))),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map("activeMode" -> Map("priceMode" -> "abc")))),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "1"), "bestPricingConfig" -> Map("abortPoint" -> "123456"))),
        JsonConfig(DayOfWeek.WEDNESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "0"))),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map("activeMode" -> Map("priceMode" -> "abc")))),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "0"), "bestPricingConfig" -> Map("abortPoint" -> "7890"))),
        JsonConfig(DayOfWeek.WEDNESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "0"))),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map("activeMode" -> Map("priceMode" -> "abc")))),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "0"), "bestPricingConfig" -> Map("abortPoint" -> "7890"))),
        JsonConfig(DayOfWeek.WEDNESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "0"))),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map("activeMode" -> Map("priceMode" -> "abc")))),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "1"), "bestPricingConfig" -> Map("abortPoint" -> "7890"))),
        JsonConfig(DayOfWeek.WEDNESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "0"))),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map("activeMode" -> Map("priceMode" -> "abc")))),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "1"), "bestPricingConfig" -> Map("abortPoint" -> "7890"))),
        JsonConfig(DayOfWeek.WEDNESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "0"))),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map("activeMode" -> Map("priceMode" -> "abc")))),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "0"), "bestPricingConfig" -> Map("abortPoint" -> "123456"))),
        JsonConfig(DayOfWeek.WEDNESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "0"))),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map("activeMode" -> Map("priceMode" -> "abc")))),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "0"), "bestPricingConfig" -> Map("abortPoint" -> "123456"))),
        JsonConfig(DayOfWeek.WEDNESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "0"))),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map("activeMode" -> Map("priceMode" -> "abc")))),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "1"), "bestPricingConfig" -> Map("abortPoint" -> "123456"))),
        JsonConfig(DayOfWeek.WEDNESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "0"))),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map("activeMode" -> Map("priceMode" -> "abc")))),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "1"), "bestPricingConfig" -> Map("abortPoint" -> "123456"))),
        JsonConfig(DayOfWeek.WEDNESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "0"))),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map("activeMode" -> Map("priceMode" -> "abc")))),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "0"), "bestPricingConfig" -> Map("abortPoint" -> "7890"))),
        JsonConfig(DayOfWeek.WEDNESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "0"))),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map("activeMode" -> Map("priceMode" -> "abc")))),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "0"), "bestPricingConfig" -> Map("abortPoint" -> "7890"))),
        JsonConfig(DayOfWeek.WEDNESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "0"))),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map("activeMode" -> Map("priceMode" -> "abc")))),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "1"), "bestPricingConfig" -> Map("abortPoint" -> "7890"))),
        JsonConfig(DayOfWeek.WEDNESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "0"))),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map("activeMode" -> Map("priceMode" -> "abc")))),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "1"), "bestPricingConfig" -> Map("abortPoint" -> "7890"))),
        JsonConfig(DayOfWeek.WEDNESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "0"))),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map("activeMode" -> Map("priceMode" -> "abc")))),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "0"), "bestPricingConfig" -> Map("abortPoint" -> "123456"))),
        JsonConfig(DayOfWeek.WEDNESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "1"))),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map("activeMode" -> Map("priceMode" -> "abc")))),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "0"), "bestPricingConfig" -> Map("abortPoint" -> "123456"))),
        JsonConfig(DayOfWeek.WEDNESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "1"))),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map("activeMode" -> Map("priceMode" -> "abc")))),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "1"), "bestPricingConfig" -> Map("abortPoint" -> "123456"))),
        JsonConfig(DayOfWeek.WEDNESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "1"))),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map("activeMode" -> Map("priceMode" -> "abc")))),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "1"), "bestPricingConfig" -> Map("abortPoint" -> "123456"))),
        JsonConfig(DayOfWeek.WEDNESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "1"))),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map("activeMode" -> Map("priceMode" -> "abc")))),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "0"), "bestPricingConfig" -> Map("abortPoint" -> "7890"))),
        JsonConfig(DayOfWeek.WEDNESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "1"))),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map("activeMode" -> Map("priceMode" -> "abc")))),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "0"), "bestPricingConfig" -> Map("abortPoint" -> "7890"))),
        JsonConfig(DayOfWeek.WEDNESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "1"))),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map("activeMode" -> Map("priceMode" -> "abc")))),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "1"), "bestPricingConfig" -> Map("abortPoint" -> "7890"))),
        JsonConfig(DayOfWeek.WEDNESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "1"))),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map("activeMode" -> Map("priceMode" -> "abc")))),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "1"), "bestPricingConfig" -> Map("abortPoint" -> "7890"))),
        JsonConfig(DayOfWeek.WEDNESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "1"))),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map("activeMode" -> Map("priceMode" -> "abc")))),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "0"), "bestPricingConfig" -> Map("abortPoint" -> "123456"))),
        JsonConfig(DayOfWeek.WEDNESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "1"))),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map("activeMode" -> Map("priceMode" -> "abc")))),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "0"), "bestPricingConfig" -> Map("abortPoint" -> "123456"))),
        JsonConfig(DayOfWeek.WEDNESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "1"))),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map("activeMode" -> Map("priceMode" -> "abc")))),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "1"), "bestPricingConfig" -> Map("abortPoint" -> "123456"))),
        JsonConfig(DayOfWeek.WEDNESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "1"))),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map("activeMode" -> Map("priceMode" -> "abc")))),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "1"), "bestPricingConfig" -> Map("abortPoint" -> "123456"))),
        JsonConfig(DayOfWeek.WEDNESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "1"))),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map("activeMode" -> Map("priceMode" -> "abc")))),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "0"), "bestPricingConfig" -> Map("abortPoint" -> "7890"))),
        JsonConfig(DayOfWeek.WEDNESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "1"))),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map("activeMode" -> Map("priceMode" -> "abc")))),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "0"), "bestPricingConfig" -> Map("abortPoint" -> "7890"))),
        JsonConfig(DayOfWeek.WEDNESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "1"))),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map("activeMode" -> Map("priceMode" -> "abc")))),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "1"), "bestPricingConfig" -> Map("abortPoint" -> "7890"))),
        JsonConfig(DayOfWeek.WEDNESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "1"))),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map("activeMode" -> Map("priceMode" -> "abc")))),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "1"), "bestPricingConfig" -> Map("abortPoint" -> "7890"))),
        JsonConfig(DayOfWeek.WEDNESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "1"))),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map("activeMode" -> Map("priceMode" -> "abc")))),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "0"), "bestPricingConfig" -> Map("abortPoint" -> "123456"))),
        JsonConfig(DayOfWeek.WEDNESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "0"))),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map("activeMode" -> Map("priceMode" -> "def")))),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "0"), "bestPricingConfig" -> Map("abortPoint" -> "123456"))),
        JsonConfig(DayOfWeek.WEDNESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "0"))),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map("activeMode" -> Map("priceMode" -> "def")))),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "1"), "bestPricingConfig" -> Map("abortPoint" -> "123456"))),
        JsonConfig(DayOfWeek.WEDNESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "0"))),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map("activeMode" -> Map("priceMode" -> "def")))),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "1"), "bestPricingConfig" -> Map("abortPoint"-> "123456"))),
        JsonConfig(DayOfWeek.WEDNESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "0"))),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map("activeMode" -> Map("priceMode" -> "def")))),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "0"), "bestPricingConfig" -> Map("abortPoint" -> "7890"))),
        JsonConfig(DayOfWeek.WEDNESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "0"))),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map("activeMode" -> Map("priceMode" -> "def")))),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "0"), "bestPricingConfig" -> Map("abortPoint" -> "7890"))),
        JsonConfig(DayOfWeek.WEDNESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "0"))),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map("activeMode" -> Map("priceMode" -> "def")))),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "1"), "bestPricingConfig" -> Map("abortPoint" -> "7890"))),
        JsonConfig(DayOfWeek.WEDNESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "0"))),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map("activeMode" -> Map("priceMode" -> "def")))),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled"-> "1", "yoseIsEnabled" -> "1"), "bestPricingConfig" -> Map("abortPoint" -> "7890"))),
        JsonConfig(DayOfWeek.WEDNESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "0"))),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map("activeMode" -> Map("priceMode" -> "def")))),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "0"), "bestPricingConfig" -> Map("abortPoint" -> "123456"))),
        JsonConfig(DayOfWeek.WEDNESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "0"))),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map("activeMode" -> Map("priceMode" -> "def")))),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "0"), "bestPricingConfig" -> Map("abortPoint" -> "123456"))),
        JsonConfig(DayOfWeek.WEDNESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "0"))),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map("activeMode" -> Map("priceMode" -> "def")))),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "1"), "bestPricingConfig" -> Map("abortPoint" -> "123456"))),
        JsonConfig(DayOfWeek.WEDNESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "0"))),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map("activeMode" -> Map("priceMode" -> "def")))),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "1"), "bestPricingConfig" -> Map("abortPoint" -> "123456"))),
        JsonConfig(DayOfWeek.WEDNESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "0"))),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map("activeMode" -> Map("priceMode" -> "def")))),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "0"), "bestPricingConfig" -> Map("abortPoint" -> "7890"))),
        JsonConfig(DayOfWeek.WEDNESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "0"))),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map("activeMode" -> Map("priceMode" -> "def")))),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "0"), "bestPricingConfig" -> Map("abortPoint" -> "7890"))),
        JsonConfig(DayOfWeek.WEDNESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "0"))),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map("activeMode" -> Map("priceMode" -> "def")))),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "1"), "bestPricingConfig" -> Map("abortPoint" -> "7890"))),
        JsonConfig(DayOfWeek.WEDNESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "0"))),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map("activeMode" -> Map("priceMode" -> "def")))),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "1"), "bestPricingConfig" -> Map("abortPoint" -> "7890"))),
        JsonConfig(DayOfWeek.WEDNESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "0"))),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map("activeMode" -> Map("priceMode" -> "def")))),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "0"), "bestPricingConfig" -> Map("abortPoint" -> "123456"))),
        JsonConfig(DayOfWeek.WEDNESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "1"))),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map("activeMode" -> Map("priceMode" -> "def")))),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "0"), "bestPricingConfig" -> Map("abortPoint" -> "123456"))),
        JsonConfig(DayOfWeek.WEDNESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "1"))),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map("activeMode" -> Map("priceMode" -> "def")))),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "1"), "bestPricingConfig" -> Map("abortPoint" -> "123456"))),
        JsonConfig(DayOfWeek.WEDNESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "1"))),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map("activeMode" -> Map("priceMode" -> "def")))),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "1"), "bestPricingConfig" -> Map("abortPoint" -> "123456"))),
        JsonConfig(DayOfWeek.WEDNESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "1"))),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map("activeMode" -> Map("priceMode" -> "def")))),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "0"), "bestPricingConfig" -> Map("abortPoint" -> "7890"))),
        JsonConfig(DayOfWeek.WEDNESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "1"))),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map("activeMode" -> Map("priceMode" -> "def")))),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "0"), "bestPricingConfig" -> Map("abortPoint" -> "7890"))),
        JsonConfig(DayOfWeek.WEDNESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "1"))),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map("activeMode" -> Map("priceMode" -> "def")))),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "1"), "bestPricingConfig" -> Map("abortPoint" -> "7890"))),
        JsonConfig(DayOfWeek.WEDNESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "1"))),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map("activeMode" -> Map("priceMode" -> "def")))),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "1"), "bestPricingConfig" -> Map("abortPoint" -> "7890"))),
        JsonConfig(DayOfWeek.WEDNESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled"-> "0", "yoseIsEnabled" -> "1"))),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map("activeMode" -> Map("priceMode" -> "def")))),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "0"), "bestPricingConfig" -> Map("abortPoint" -> "123456"))),
        JsonConfig(DayOfWeek.WEDNESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "1"))),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map("activeMode" -> Map("priceMode"-> "def")))),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "0"), "bestPricingConfig" -> Map("abortPoint" -> "123456"))),
        JsonConfig(DayOfWeek.WEDNESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "1"))),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map("activeMode" -> Map("priceMode" -> "def")))),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "1"), "bestPricingConfig" -> Map("abortPoint" -> "123456"))),
        JsonConfig(DayOfWeek.WEDNESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "1"))),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map("activeMode" -> Map("priceMode" -> "def")))),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "1"), "bestPricingConfig" -> Map("abortPoint" -> "123456"))),
        JsonConfig(DayOfWeek.WEDNESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "1"))),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map("activeMode" -> Map("priceMode" -> "def")))),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "0"), "bestPricingConfig" -> Map("abortPoint" -> "7890"))),
        JsonConfig(DayOfWeek.WEDNESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "1"))),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map("activeMode" -> Map("priceMode" -> "def")))),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "0"), "bestPricingConfig" -> Map("abortPoint" -> "7890"))),
        JsonConfig(DayOfWeek.WEDNESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "1"))),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map("activeMode" -> Map("priceMode" -> "def")))),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "1"), "bestPricingConfig" -> Map("abortPoint" -> "7890"))),
        JsonConfig(DayOfWeek.WEDNESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "1"))),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map("activeMode" -> Map("priceMode" -> "def")))),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "1"), "bestPricingConfig" -> Map("abortPoint" -> "7890"))),
        JsonConfig(DayOfWeek.WEDNESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "1"))),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map("activeMode" -> Map("priceMode" -> "def")))))
    assert(parseResult == answer)
  }


  /**
    * config空がある場合
    */
  "jsonParamParser" should "parse correctly5" in {
    val source =
      """
          [
            {
              "since": "1/00:00:00",
              "config": {
                "customerPricingConfig": {
                  "ktyrIsEnabled": [0,1],
                  "yoseIsEnabled":  [0,1]
                },
                "bestPricingConfig": {
                   "abortPoint": [123456]
                 }
              }
            },
            {
              "since": "2/00:00:00",
              "config": {
                "customerPricingConfig": {
                }
              }
            },
            {
              "since": "2/08:30:00",
              "config": {
                "customerPricingConfig": {
                   "ktyrIsEnabled":  [0,1],
                   "yoseIsEnabled": [  0,   "1"]
                }
              }
            },
            {
              "since": "4/12:30:25",
              "config": {
              }
            },
            {
              "since": "5/06:00:00",
              "config": {
                "activeMode": {
                    "priceMode":  ["abc","def"]
                 }
              }
            }
          ]
        """
    val parseResult = JsonParamParser.loadJsonParam(source)
    val answer = List(
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "0"), "bestPricingConfig" -> Map("abortPoint" -> "123456"))),
        JsonConfig(DayOfWeek.TUESDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map())),
        JsonConfig(DayOfWeek.TUESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "0"))),
        JsonConfig(DayOfWeek.THURSDAY,LocalTime.parse("12:30:25"),Map()),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map("activeMode" -> Map("priceMode" -> "abc")))),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "0"), "bestPricingConfig" -> Map("abortPoint" -> "123456"))),
        JsonConfig(DayOfWeek.TUESDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map())),
        JsonConfig(DayOfWeek.TUESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "0"))),
        JsonConfig(DayOfWeek.THURSDAY,LocalTime.parse("12:30:25"),Map()),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map("activeMode" -> Map("priceMode" -> "abc")))),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "1"), "bestPricingConfig" -> Map("abortPoint" -> "123456"))),
        JsonConfig(DayOfWeek.TUESDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map())),
        JsonConfig(DayOfWeek.TUESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "0"))),
        JsonConfig(DayOfWeek.THURSDAY,LocalTime.parse("12:30:25"),Map()),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map("activeMode" -> Map("priceMode" -> "abc")))),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "1"), "bestPricingConfig" -> Map("abortPoint" -> "123456"))),
        JsonConfig(DayOfWeek.TUESDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map())),
        JsonConfig(DayOfWeek.TUESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "0"))),
        JsonConfig(DayOfWeek.THURSDAY,LocalTime.parse("12:30:25"),Map()),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map("activeMode" -> Map("priceMode" -> "abc")))),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "0"), "bestPricingConfig" -> Map("abortPoint" -> "123456"))),
        JsonConfig(DayOfWeek.TUESDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map())),
        JsonConfig(DayOfWeek.TUESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "0"))),
        JsonConfig(DayOfWeek.THURSDAY,LocalTime.parse("12:30:25"),Map()),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map("activeMode" -> Map("priceMode" -> "abc")))),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "0"), "bestPricingConfig" -> Map("abortPoint" -> "123456"))),
        JsonConfig(DayOfWeek.TUESDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map())),
        JsonConfig(DayOfWeek.TUESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "0"))),
        JsonConfig(DayOfWeek.THURSDAY,LocalTime.parse("12:30:25"),Map()),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map("activeMode" -> Map("priceMode" -> "abc")))),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "1"), "bestPricingConfig" -> Map("abortPoint" -> "123456"))),
        JsonConfig(DayOfWeek.TUESDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map())),
        JsonConfig(DayOfWeek.TUESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "0"))),
        JsonConfig(DayOfWeek.THURSDAY,LocalTime.parse("12:30:25"),Map()),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map("activeMode" -> Map("priceMode" -> "abc")))),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "1"), "bestPricingConfig" -> Map("abortPoint" -> "123456"))),
        JsonConfig(DayOfWeek.TUESDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map())),
        JsonConfig(DayOfWeek.TUESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "0"))),
        JsonConfig(DayOfWeek.THURSDAY,LocalTime.parse("12:30:25"),Map()),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map("activeMode" -> Map("priceMode" -> "abc")))),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "0"), "bestPricingConfig" -> Map("abortPoint" -> "123456"))),
        JsonConfig(DayOfWeek.TUESDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map())),
        JsonConfig(DayOfWeek.TUESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "1"))),
        JsonConfig(DayOfWeek.THURSDAY,LocalTime.parse("12:30:25"),Map()),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map("activeMode" -> Map("priceMode" -> "abc")))),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "0"), "bestPricingConfig" -> Map("abortPoint" -> "123456"))),
        JsonConfig(DayOfWeek.TUESDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map())),
        JsonConfig(DayOfWeek.TUESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "1"))),
        JsonConfig(DayOfWeek.THURSDAY,LocalTime.parse("12:30:25"),Map()),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map("activeMode" -> Map("priceMode" -> "abc")))),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "1"), "bestPricingConfig" -> Map("abortPoint" -> "123456"))),
        JsonConfig(DayOfWeek.TUESDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map())),
        JsonConfig(DayOfWeek.TUESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "1"))),
        JsonConfig(DayOfWeek.THURSDAY,LocalTime.parse("12:30:25"),Map()),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map("activeMode" -> Map("priceMode" -> "abc")))),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "1"), "bestPricingConfig" -> Map("abortPoint" -> "123456"))),
        JsonConfig(DayOfWeek.TUESDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map())),
        JsonConfig(DayOfWeek.TUESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "1"))),
        JsonConfig(DayOfWeek.THURSDAY,LocalTime.parse("12:30:25"),Map()),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map("activeMode" -> Map("priceMode" -> "abc")))),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "0"), "bestPricingConfig" -> Map("abortPoint" -> "123456"))),
        JsonConfig(DayOfWeek.TUESDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map())),
        JsonConfig(DayOfWeek.TUESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "1"))),
        JsonConfig(DayOfWeek.THURSDAY,LocalTime.parse("12:30:25"),Map()),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map("activeMode" -> Map("priceMode" -> "abc")))),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "0"), "bestPricingConfig" -> Map("abortPoint" -> "123456"))),
        JsonConfig(DayOfWeek.TUESDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map())),
        JsonConfig(DayOfWeek.TUESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "1"))),
        JsonConfig(DayOfWeek.THURSDAY,LocalTime.parse("12:30:25"),Map()),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map("activeMode" -> Map("priceMode" -> "abc")))),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "1"), "bestPricingConfig" -> Map("abortPoint" -> "123456"))),
        JsonConfig(DayOfWeek.TUESDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map())),
        JsonConfig(DayOfWeek.TUESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "1"))),
        JsonConfig(DayOfWeek.THURSDAY,LocalTime.parse("12:30:25"),Map()),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map("activeMode" -> Map("priceMode" -> "abc")))),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "1"), "bestPricingConfig" -> Map("abortPoint" -> "123456"))),
        JsonConfig(DayOfWeek.TUESDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map())),
        JsonConfig(DayOfWeek.TUESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "1"))),
        JsonConfig(DayOfWeek.THURSDAY,LocalTime.parse("12:30:25"),Map()),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map("activeMode" -> Map("priceMode" -> "abc")))),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "0"), "bestPricingConfig" -> Map("abortPoint" -> "123456"))),
        JsonConfig(DayOfWeek.TUESDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map())),
        JsonConfig(DayOfWeek.TUESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "0"))),
        JsonConfig(DayOfWeek.THURSDAY,LocalTime.parse("12:30:25"),Map()),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map("activeMode" -> Map("priceMode" -> "def")))),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "0"), "bestPricingConfig" -> Map("abortPoint" -> "123456"))),
        JsonConfig(DayOfWeek.TUESDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map())),
        JsonConfig(DayOfWeek.TUESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "0"))),
        JsonConfig(DayOfWeek.THURSDAY,LocalTime.parse("12:30:25"),Map()),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map("activeMode" -> Map("priceMode" -> "def")))),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "1"), "bestPricingConfig" -> Map("abortPoint" -> "123456"))),
        JsonConfig(DayOfWeek.TUESDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map())),
        JsonConfig(DayOfWeek.TUESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "0"))),
        JsonConfig(DayOfWeek.THURSDAY,LocalTime.parse("12:30:25"),Map()),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map("activeMode" -> Map("priceMode" -> "def")))),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "1"), "bestPricingConfig" -> Map("abortPoint" -> "123456"))),
        JsonConfig(DayOfWeek.TUESDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map())),
        JsonConfig(DayOfWeek.TUESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "0"))),
        JsonConfig(DayOfWeek.THURSDAY,LocalTime.parse("12:30:25"),Map()),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map("activeMode" -> Map("priceMode" -> "def")))),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "0"), "bestPricingConfig" -> Map("abortPoint" -> "123456"))),
        JsonConfig(DayOfWeek.TUESDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map())),
        JsonConfig(DayOfWeek.TUESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "0"))),
        JsonConfig(DayOfWeek.THURSDAY,LocalTime.parse("12:30:25"),Map()),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map("activeMode" -> Map("priceMode" -> "def")))),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "0"), "bestPricingConfig" -> Map("abortPoint" -> "123456"))),
        JsonConfig(DayOfWeek.TUESDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map())),
        JsonConfig(DayOfWeek.TUESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "0"))),
        JsonConfig(DayOfWeek.THURSDAY,LocalTime.parse("12:30:25"),Map()),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map("activeMode" -> Map("priceMode" -> "def")))),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "1"), "bestPricingConfig" -> Map("abortPoint" -> "123456"))),
        JsonConfig(DayOfWeek.TUESDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map())),
        JsonConfig(DayOfWeek.TUESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "0"))),
        JsonConfig(DayOfWeek.THURSDAY,LocalTime.parse("12:30:25"),Map()),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map("activeMode" -> Map("priceMode" -> "def")))),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "1"), "bestPricingConfig" -> Map("abortPoint" -> "123456"))),
        JsonConfig(DayOfWeek.TUESDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map())),
        JsonConfig(DayOfWeek.TUESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "0"))),
        JsonConfig(DayOfWeek.THURSDAY,LocalTime.parse("12:30:25"),Map()),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map("activeMode" -> Map("priceMode" -> "def")))),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "0"), "bestPricingConfig" -> Map("abortPoint" ->"123456"))),
        JsonConfig(DayOfWeek.TUESDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map())),
        JsonConfig(DayOfWeek.TUESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "1"))),
        JsonConfig(DayOfWeek.THURSDAY,LocalTime.parse("12:30:25"),Map()),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map("activeMode" -> Map("priceMode" -> "def")))),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "0"), "bestPricingConfig" -> Map("abortPoint" -> "123456"))),
        JsonConfig(DayOfWeek.TUESDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map())),
        JsonConfig(DayOfWeek.TUESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "1"))),
        JsonConfig(DayOfWeek.THURSDAY,LocalTime.parse("12:30:25"),Map()),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map("activeMode" -> Map("priceMode" -> "def")))),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "1"), "bestPricingConfig" -> Map("abortPoint" -> "123456"))),
        JsonConfig(DayOfWeek.TUESDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map())),
        JsonConfig(DayOfWeek.TUESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "1"))),
        JsonConfig(DayOfWeek.THURSDAY,LocalTime.parse("12:30:25"),Map()),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map("activeMode" -> Map("priceMode" -> "def")))),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "1"), "bestPricingConfig" -> Map("abortPoint" -> "123456"))),
        JsonConfig(DayOfWeek.TUESDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map())),
        JsonConfig(DayOfWeek.TUESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "1"))),
        JsonConfig(DayOfWeek.THURSDAY,LocalTime.parse("12:30:25"),Map()),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map("activeMode" -> Map("priceMode" -> "def")))),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "0", "yoseIsEnabled" -> "0"), "bestPricingConfig" -> Map("abortPoint" -> "123456"))),
        JsonConfig(DayOfWeek.TUESDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map())),
        JsonConfig(DayOfWeek.TUESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "1"))),
        JsonConfig(DayOfWeek.THURSDAY,LocalTime.parse("12:30:25"),Map()),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map("activeMode" -> Map("priceMode" -> "def")))),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "0"), "bestPricingConfig" -> Map("abortPoint" -> "123456"))),
        JsonConfig(DayOfWeek.TUESDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map())),
        JsonConfig(DayOfWeek.TUESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "1"))),
        JsonConfig(DayOfWeek.THURSDAY,LocalTime.parse("12:30:25"),Map()),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map("activeMode" -> Map("priceMode" -> "def")))),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled"-> "0", "yoseIsEnabled" -> "1"), "bestPricingConfig" -> Map("abortPoint" -> "123456"))),
        JsonConfig(DayOfWeek.TUESDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map())),
        JsonConfig(DayOfWeek.TUESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "1"))),
        JsonConfig(DayOfWeek.THURSDAY,LocalTime.parse("12:30:25"),Map()),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map("activeMode" -> Map("priceMode" -> "def")))),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "1"), "bestPricingConfig" -> Map("abortPoint" -> "123456"))),
        JsonConfig(DayOfWeek.TUESDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map())),
        JsonConfig(DayOfWeek.TUESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "1", "yoseIsEnabled" -> "1"))),
        JsonConfig(DayOfWeek.THURSDAY,LocalTime.parse("12:30:25"),Map()),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map("activeMode" -> Map("priceMode" -> "def"))))
    )
    assert(parseResult == answer)
  }


  /**
    * 最初の設定が空の場合に正常に動作するか確認1
    */
  "jsonParamParser" should "parse correctly6" in {
    val source =
      """
          [
            {
              "since": "1/00:00:00",
              "config": {
              }
            }
          ]
        """
    val parseResult = JsonParamParser.loadJsonParam(source)
    val answer = List(
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map())
      )
    )
    assert(parseResult == answer)
  }

  /**
    * 最初の設定が空の場合に正常に動作するか確認2
    */
  "jsonParamParser" should "parse correctly7" in {
    val source =
      """
          [
            {
              "since": "1/00:00:00",
              "config": {
                "customerPricingConfig": {
                }
              }
            },
            {
              "since": "3/08:30:00",
              "config": {
                "customerPricingConfig": {
                   "ktyrIsEnabled": [3, 4],
                   "yoseIsEnabled": 4
                }
              }
            },
            {
              "since": "5/06:00:00",
              "config": {
              }
            }
          ]
        """
    val parseResult = JsonParamParser.loadJsonParam(source)

    val answer = List(
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map())),
        JsonConfig(DayOfWeek.WEDNESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "3", "yoseIsEnabled" -> "4"))),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map())
      ),
      List(
        JsonConfig(DayOfWeek.MONDAY,LocalTime.parse("00:00"),Map("customerPricingConfig" -> Map())),
        JsonConfig(DayOfWeek.WEDNESDAY,LocalTime.parse("08:30"),Map("customerPricingConfig" -> Map("ktyrIsEnabled" -> "4", "yoseIsEnabled" -> "4"))),
        JsonConfig(DayOfWeek.FRIDAY,LocalTime.parse("06:00"),Map())
      )
    )
    assert(parseResult == answer)
  }

  /**
    * json4sでのパース失敗時にエラーを投げること
    */
  "jsonParamParser" should "throw error1" in {
    var success = 0
    val source =
      """
          [
            {
              "since": "1/00:00:00",
              "config": {
                "customerPricingConfig": {
                  "ktyrIsEnabled": 0,
                  "yoseIsEnabled": 1,
                }
              }
            }
          ]
        """
    try{
      JsonParamParser.loadJsonParam(source)
    } catch {
      case e:ParameterLoaderException => success = 1
    }
    assert(success == 1)
  }

  /**
    * sinceとconfigのフォーマットとあってない場合にエラーが投げられることを確認
    */
  "jsonParamParser" should "throw error2" in {
    var success = 0
    val source =
      """
          [
            {
              "since": "1/00:00:00",
              "config_error": {
                "customerPricingConfig": {
                  "ktyrIsEnabled": 0,
                  "yoseIsEnabled": 1
                }
              }
            }
          ]
        """
    try{
      JsonParamParser.loadJsonParam(source)
    } catch {
      case e:JsonParameterInvalid => success = 1
    }
    assert(success == 1)
  }

  /**
    * sinceの時間のフォーマットが異なる場合にエラーが投げられることを確認
    */
  "jsonParamParser" should "throw error3" in {
    var success = 0
    val source =
      """
          [
            {
              "since": "1 00:00:00",
              "config": {
                "customerPricingConfig": {
                  "ktyrIsEnabled": 0,
                  "yoseIsEnabled": 1
                }
              }
            }
          ]
        """
    try{
      JsonParamParser.loadJsonParam(source)
    } catch {
      case e:JsonParameterInvalid => success = 1
    }
    assert(success == 1)
  }

  /**
    * configのフォーマットが異なる場合にエラーが投げられることを確認
    */
  "jsonParamParser" should "throw error4" in {
    var success = 0
    val source =
      """
          [
            {
              "since": "1/00:00:00",
              "config": {
                "ktyrIsEnabled": 0
              }
            }
          ]
        """
    try{
      JsonParamParser.loadJsonParam(source)
    } catch {
      case e:JsonParameterInvalid => success = 1
    }
    assert(success == 1)
  }

  /**
    * configのフォーマットが異なる場合にエラーが投げられることを確認
    */
  "jsonParamParser" should "throw error5" in {
    var success = 0
    val source =
      """
          [
            {
              "since": "1/00:00:00",
              "config": "100"
            }
          ]
        """
    try{
      JsonParamParser.loadJsonParam(source)
    } catch {
      case e:JsonParameterInvalid => success = 1
    }
    assert(success == 1)
  }



  /**
    * configのフォーマットが異なる場合にエラーが投げられることを確認
    */
  "jsonParamParser" should "throw error6" in {
    var success = 0
    val source =
      """
           [{
              "since": "100/08:30:00",
              "config": {
                "customerPricingConfig": {
                   "ktyrIsEnabled": [3, 4],
                   "yoseIsEnabled": 4
                }
              }
            }]
        """
    try{
      JsonParamParser.loadJsonParam(source)
    } catch {
      case e:ParameterLoaderException => success = 1
    }
    assert(success == 1)
  }
  /**
    * configのフォーマットが異なる場合にエラーが投げられることを確認
    */
  "jsonParamParser" should "throw error7" in {
    var success = 0
    val source =
      """
           [{
              "since": "a/08:30:00",
              "config": {
                "customerPricingConfig": {
                   "ktyrIsEnabled": [3, 4],
                   "yoseIsEnabled": 4
                }
              }
            }]
        """
    try{
      JsonParamParser.loadJsonParam(source)
    } catch {
      case e:ParameterLoaderException => success = 1
    }
    assert(success == 1)
  }
}