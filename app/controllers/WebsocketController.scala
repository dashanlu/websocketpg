package controllers

import actor.{MyWebSocketActor, PersonArrowDataWebSocketActor}
import akka.actor.ActorSystem
import akka.stream.Materializer
import akka.stream.scaladsl.{Flow, Sink, Source}
import akka.util.ByteString
import com.google.inject.Singleton
import javax.inject.Inject
import play.api.libs.streams.ActorFlow
import play.api.mvc._

@Singleton
class WebsocketController @Inject()(cc: ControllerComponents, actorSystem: ActorSystem)(implicit system: ActorSystem, mat: Materializer) extends AbstractController(cc) {

  def broadcast = WebSocket.accept[String, String] { request =>
    ActorFlow.actorRef { out =>
      MyWebSocketActor.props(out)
    }
  }

  def broadcast1 = WebSocket.accept[ByteString, ByteString] { request =>
    ActorFlow.actorRef { out =>
      PersonArrowDataWebSocketActor.props(out)
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
