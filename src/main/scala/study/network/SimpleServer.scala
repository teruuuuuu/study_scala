package study.network

import akka.actor._
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpRequest
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import spray.json.DefaultJsonProtocol._


object HelloApiServer extends App {

  // JSONをマッピングするケースクラス
  case class HelloRequest(name: String)
  case class HelloResponse(message: String)

  // JSON変換に必要なJsonFormatを定義
  implicit val helloRequestProtocol = jsonFormat1(HelloRequest)
  implicit val helloResponseProtocol = jsonFormat1(HelloResponse)

  implicit val actorSystem = ActorSystem("my-system")
  implicit val flowMaterializer = ActorMaterializer()

  val route = get {
    pathEndOrSingleSlash {
      handleWith((request: HttpRequest) => "API is ready.")
    }
  } ~ path("hello") {
    post {
      entity(as[HelloRequest]){ request =>
        complete {
          HelloResponse(message = s"Hello, ${request.name}!")
        }
      }
    }
  } ~ path("test") {
    get {
      handleWith((a: HttpRequest) => s"your request is\n\n${a.headers.mkString("\n")}")
    }
    post {
      handleWith((a: HttpRequest) => s"your request is\n\n${a.headers.mkString("\n")}")
    }
  }

  val serverBinding = Http(actorSystem)
    .bindAndHandle(route, interface = "localhost", port = 8080)

}