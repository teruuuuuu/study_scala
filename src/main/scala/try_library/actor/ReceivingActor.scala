package try_library.actor

import akka.actor.{Actor, ActorLogging, Props}
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.Future


class ReceivingActor extends Actor with ActorLogging{

  var count:Int = 1

  def receive = {
    case MessagingActor.SendMessage(time: String) =>
      println(time + " message receive " + count)
      count += 1

      val f1 = Future(wait1(1000))
      val f2 = Future(wait2(1000))

      val ret = for {
        x <- f1
        y <- f2
      } yield f(x, y)
    case MessagingActor.SendMessage1(time: String) =>
      wait1(1000)
    case MessagingActor.SendMessage2(time: String) =>
      wait2(1000)
  }
  def wait1(time: Int): Unit ={
    println("wait1 start")
    Thread.sleep(time)
    println("wait1 end")
  }
  def wait2(time: Int): Unit ={
    println("wait2 start")
    Thread.sleep(time)
    println("wait2 end")
  }
  def f(x:Any,y:Any){println("call yield")}

  def ShowMessage(text: String) = {
    println(text)
  }
}

object ReceivingActor {
  val props = Props[ReceivingActor]
}