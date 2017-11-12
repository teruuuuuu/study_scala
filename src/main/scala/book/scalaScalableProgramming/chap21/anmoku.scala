package book.scalaScalableProgramming.chap21

import java.awt.event.{ActionEvent, ActionListener}
import javax.swing.JButton

/**
  * Created by arimuraterutoshi on 2017/01/09.
  */
object anmoku1 {
  val button = new JButton

  button.addActionListener(
    // 暗黙の型変換によりアクションイベントをUnitに変更した上で関数を呼び出している
    function2ActionListener(
      (_: ActionEvent) => println("passed")
    )
  )

  implicit def function2ActionListener(f: ActionEvent => Unit) =
    new ActionListener {
      def actionPerformed(event: ActionEvent) = f(event)
    }
}

trait myListener{
  implicit def function2ActionListener(f: ActionEvent => Unit) =
    new ActionListener {
      def actionPerformed(event: ActionEvent) = f(event)
    }
}

object anmoku2 extends myListener{
  val button = new JButton

  button.addActionListener(
    (_: ActionEvent) => println("passed")
  )
}

object anmoku3{
  implicit def doubleToInt(x: Double) = x.toInt

  def main(args: Array[String]): Unit ={
    // implicitがスコープないなら有効
    val i: Int = 3.5
    print(i)
  }
}


class PreferredPrompt(val preference: String)
class PreferredDrink(val preference: String)
object anmoku4{
  implicit val promt = new PreferredPrompt("yes, master>")

  // 暗黙の型変換を含んだ関数
  def greet1(name: String)(implicit prompt: PreferredPrompt): Unit ={
    println("Welcome, " + name + ". The system is ready.")
    println(prompt.preference)
  }

  def greet2(name: String)(implicit prompt: PreferredPrompt, drink: PreferredDrink): Unit ={
    println("Welcome, " + name + ". The system is ready.")
    println("But while you work..")
    println("why not enjoy a cup of " + drink.preference + "?")
    println(prompt.preference)
  }


  def main(args:Array[String]): Unit ={
    greet1("test")

    val drink = new PreferredDrink("coffey")
    greet2("drinkkkK")(promt, drink)
  }
}

object anmoku5{
  implicit val ls = "aaa" :: "bbb" :: Nil

  // 暗黙のパラメータを関数内で使用するサンプル
  def maxList1[T](elements: List[T])(implicit orderer: T => Ordered[T]): T =
    elements match{
      case List() =>
        throw new IllegalArgumentException("empty list!")
      case List(x) => x
      case x:: rest =>
        val maxRest = maxList1(rest)
        if(x > maxRest) x
        else maxRest
    }

  /*

  def maxList2[T](elements: List[T])(implicit iceCream: T => Orderd[T]): T =
    elements match{
      case List() =>
        throw new IllegalArgumentException("empty list!")
      case List(x) => x
      case x:: rest =>
        val maxRest = maxList2(rest)
        if(x > maxRest) x
        else maxRest
    }
  */

  // 可視境界を使った関数
  // T <% Orderd[T] は"[Orderd[T]として扱える任意のTを使えること
  // T <: Orderd[T] は"Tは[Orderd[T]であるということ
  /*
  def maxList3[ T <% Orderd[T]](elements: List[T]): T =
    elements match{
      case List() =>
        throw new IllegalArgumetException("empty List!")
      case List(x) => x
      case x::rest =>
        val maxRest = maxList3(rest)
        if(x > maxRest) x
        else maxRest
    }*/



  def main(args: Array[String]): Unit ={
    val lst = "aaaa" :: "bbb" :: Nil
    val max1 = maxList1(lst)
    print(max1.mkString)
  }
}