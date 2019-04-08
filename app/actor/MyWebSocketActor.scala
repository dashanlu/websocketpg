package actor

import akka.actor.{Actor, ActorRef, _}

object MyWebSocketActor {
  def props(out: ActorRef) = Props(new MyWebSocketActor(out))
}

class MyWebSocketActor(out: ActorRef) extends Actor {
  def receive = {
    case msg: String => out ! msg.toUpperCase
    //      1 to 5 foreach { x => out ! msg.toUpperCase }

  }
}