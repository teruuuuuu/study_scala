package try_library.actor

import akka.actor.{Actor, ActorSystem, Props, ReceiveTimeout}
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.xml.ScalaXmlSupport._
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer

import scala.io._


object PageLoader {
  def getPageSize(url:String) = Source.fromURL(url).mkString.length
}

class UrlRequestActor extends Actor{
  def receive ={
    case url:String => println("Size for " + url + ": " + PageLoader.getPageSize(url))
  }
}

class HelloActor extends Actor{

  //setReceiveTimeout(100 millis)

  def receive ={
    case s:String => println(s)
    case _ => print( " not string")
    case ReceiveTimeout => throw new RuntimeException //タイムアウトした場合
  }
}

object HelloActorExec{

  def main(args: Array[String]): Unit = {
    val system = ActorSystem()
    val ref = system.actorOf(Props[HelloActor], name = "helloActor")
    val greets:List[String] = List("hellow", "こんにちは", "ニーハオ", "Bonjour", "Buon giorno!", "Guten  tag")

    println("normal")
    greets.foreach(greet => println(greet))

    println("akka")
    greets.foreach(greet => ref ! greet)

    val urls:List[String] = List("http://twitter.com/",
                                 "http://edition.cnn.com/")


    val urlRef = system.actorOf(Props[UrlRequestActor], name = "UrlRequestActor")

    val start = System.nanoTime
    urls.foreach(url => urlRef ! url)
    val end = System.nanoTime
    println(" akka http request time:" + (end - start) / 10000000000.0 + "seconds" )

    val start2 = System.nanoTime
    urls.foreach(url => PageLoader.getPageSize(url))
    val end2 = System.nanoTime
    println(" normal http request time:" + (end2 - start2) / 10000000000.0 + "seconds" )
  }

}


object SimpleServer {
  def main(args: Array[String]): Unit = {

    // アクターシステムおよびその他「おまじない」を `implicit` で宣言
    implicit val system = ActorSystem("mySystem")
    implicit val materializer = ActorMaterializer()
    implicit val ec = system.dispatcher

    // ルーティング GET '/'
    val route = path("") {
      get {
        complete {
          <h1>Say hello to akka-http</h1>
        } // ↑*1
      }
    } ~ pathPrefix("piyo" / ".+".r) { str =>
      get {
        complete {
          <h1>piyo piyo {str}</h1>
        }
      }
    }

    // IP とポートを指定してリッスン開始
    val bindingFuture = Http().bindAndHandle(route, "127.0.0.1", 8080)

    // 簡単のためデーモン化せず、リターンを入力すれば停止するようにしています。
    println("Server online at http://127.0.0.1:8080/\nPress RETURN to stop...")
    scala.io.StdIn.readLine()
    bindingFuture.flatMap(_.unbind()).onComplete(_ => system.terminate())
  }
}