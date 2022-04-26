package example.actorsream

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.Behavior

object RandomIntSourceActor {
  trait Command

  def apply(): Behavior[Command] =
    Behaviors.receive { (context, message) =>
      message match {
        case _ => context.log.info("Strange message.")
      }

      Behaviors.same
    }
}
