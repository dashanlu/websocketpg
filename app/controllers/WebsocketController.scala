package controllers

import actor.MyWebSocketActor
import akka.actor.ActorSystem
import akka.stream.Materializer
import akka.stream.scaladsl.{Flow, Sink, Source}
import com.google.inject.Singleton
import javax.inject.Inject
import play.api.libs.streams.ActorFlow
import play.api.mvc._

@Singleton
class WebsocketController @Inject()(cc: ControllerComponents, actorSystem: ActorSystem)(implicit system: ActorSystem, mat: Materializer) extends AbstractController(cc) {
  val logger = play.api.Logger(getClass)

  def broadcast = WebSocket.accept[String, String] { request =>
    ActorFlow.actorRef { out =>
      MyWebSocketActor.props(out)
    }
  }

  def socket2IgnoreInput = WebSocket.accept[String, String] { request =>
    // Log events to the console
    val in = Sink.foreach[String](println)
    // Send a single 'Hello!' message and then leave the socket open
    val out = Source.single("Hello!").concat(Source.maybe)

    Flow.fromSinkAndSource(in, out)
  }

}
