package book.scalaScalableProgramming.chap14

import book.scalaScalableProgramming.chap10.Element
import book.scalaScalableProgramming.chap10.Element._

object StringUtils {

  def isEmpty(value: String):Boolean =
    value != null || value.length() == 0
}

/**
object AssertionTest{
  def main(args: Array[String]): Unit ={
    assert(1 == 1)
    // falseの場合はエラー
    assert(1 == 2)

    enshurTestOk
    enshurTestNg
  }

  def enshurTestOk: Int = {
    1
  } ensuring(1 == _)

  def enshurTestNg: Int = {
    1
  } ensuring(2 == _)

}


import collection.mutable.Stack
import org.

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

object ScalaTestExample{

  def main(args: Array[String]): Unit ={

    val es:ExampleSpec = new ExampleSpec()
  }
}
*/