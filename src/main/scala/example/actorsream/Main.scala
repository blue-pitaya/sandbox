package example.actorsream

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors

object Main extends App {
  implicit val system = ActorSystem(Behaviors.empty, "some-system")

}
