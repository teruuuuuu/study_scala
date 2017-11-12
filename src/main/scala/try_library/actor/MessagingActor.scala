package try_library.actor

import java.util.Date

import akka.actor.{Actor, ActorLogging, Props}

/**
  * Created by arimura_terutoshi on 2017/02/10.
  */
class MessagingActor extends Actor with ActorLogging{
  import MessagingActor._

  val receiveActor = context.actorOf(ReceivingActor.props, "receiveActor")
  val receiveActor1 = context.actorOf(ReceivingActor.props, "receiveActor1")
  val receiveActor2 = context.actorOf(ReceivingActor.props, "receiveActor2")

  def receive = {
    case Initialize =>
      log.info("starting MessagingActor")
    case "sendMessage" =>
      println("send message")
      receiveActor !  SendMessage("%tY/%<tm/%<td %<tH:%<tM:%<tS" format new Date)
      //receiveActor1 !  SendMessage1("%tY/%<tm/%<td %<tH:%<tM:%<tS" format new Date)
      //receiveActor2 !  SendMessage2("%tY/%<tm/%<td %<tH:%<tM:%<tS" format new Date)
  }
}

object MessagingActor {
  val props = Props[MessagingActor]
  case object Initialize
  case class SendMessage(text: String)
  case class SendMessage1(text: String)
  case class SendMessage2(text: String)
}