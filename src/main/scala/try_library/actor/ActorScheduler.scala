package try_library.actor

import akka.actor.ActorSystem
import com.typesafe.akka.extension.quartz.QuartzSchedulerExtension

object ActorScheduler extends App {

  val _system = ActorSystem("system")
  // スケジューラを生成
  val scheduler = QuartzSchedulerExtension(_system)
  val messagingActor = _system.actorOf(MessagingActor.props, "messagingActor")
  scheduler.schedule(
    "MessageTask",
    messagingActor,
    "sendMessage"
  )
}