package example.akka.clustersharding

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.ActorRef

object UserCounter {
  sealed trait Command
  final case object Increment extends Command
  final case object Decrement extends Command
  final case class GetValue(replyTo: ActorRef[CounterReply]) extends Command

  final case class CounterReply(value: Int)

  private def running(counterValue: Int): Behavior[Command] =
    Behaviors.receive { (context, command) =>
      command match {
        case Increment => running(counterValue + 1)
        case Decrement => running(counterValue - 1)
        case GetValue(replyTo) =>
          replyTo ! CounterReply(counterValue)
          Behaviors.same
      }

    }

  //id is not required here
  def apply(id: String): Behavior[Command] = running(0)
}
