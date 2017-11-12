package book.scalaScalableProgramming.chap14


import collection.mutable.Stack
import org.scalatest._

class ExampleSpec extends FlatSpec with Matchers {

  "A Stack" should "pop values in last-in-first-out order" in {
    val stack = new Stack[Int]
    stack.push(1)
    stack.push(2)
    stack.pop() should be (2)
    stack.pop() should be (1)
  }

  it should "throw NoSuchElementException if an empty stack is popped" in {
    val emptyStack = new Stack[Int]
    a [NoSuchElementException] should be thrownBy {
      emptyStack.pop()
    }
  }

}

class StringUtilsSuite extends FunSuite{

  // 引数nullの場合にtrueg帰ることを確認する
  test("StringUtils#isEmpty returns true for null"){
    assert(StringUtils.isEmpty(null))
  }

  //引数がから文字列の場合にtrueが返ることを確認する
  test("StringUtils#isEmpty returns true for the empty String"){
    assert(StringUtils.isEmpty(""))
  }

  //引数が任意の文字列の場合に falseがかけることを確認する
  test("StringUtils#isEmpty returns false for any other String"){
    assert(!StringUtils.isEmpty("a"))
  }
}