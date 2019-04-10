package actor

import akka.actor.{Actor, ActorRef, Props, _}
import akka.util.ByteString
import services.ArrowDataGenerator

object PersonArrowDataWebSocketActor {

  import services.PersonArrowDataGenerator

  implicit val arrowDataGenerator = new PersonArrowDataGenerator()

  def props(out: ActorRef) = Props(new PersonArrowDataWebSocketActor(out))

}

case class Person(name: String, age: Int)

class PersonArrowDataWebSocketActor(out: ActorRef)(implicit generator: ArrowDataGenerator) extends Actor {
  private val logger = play.api.Logger(getClass)

  def receive = {
    case msg: ByteString => {
      val utf8StringVal = msg.utf8String.toUpperCase;
      logger.info(s"receiving $utf8StringVal")
      val respStringByte = generator.generate()
      logger.info(s"sending arrow data back")
      out ! respStringByte
    }

  }
}








