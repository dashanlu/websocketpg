package actor

import akka.actor.{Actor, ActorRef, _}
import akka.util.ByteString

object MyArrowDataWebSocketActor {
  def props(out: ActorRef) = Props(new MyArrowDataWebSocketActor(out))
}


class MyArrowDataWebSocketActor(out: ActorRef) extends Actor {
  def receive = {
    case msg: ByteString => {
      val utf8StringVal = msg.utf8String.toUpperCase;
      println(utf8StringVal)
      out ! ByteString(utf8StringVal.toUpperCase())
    }

  }
}