package example.akka.general

import akka.actor.typed.ActorRef
import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors

object InMemoryKeyValueRepository {
  sealed trait Command[TId, TData]
  final case class Create[TId, TData](id: TId, value: TData, replyTo: ActorRef[Reply]) extends Command[TId, TData]
  final case class Read[TId, TData](id: TId, replyTo: ActorRef[Reply]) extends Command[TId, TData]

  sealed trait Reply //TODO: single Reply type for command
  final case object Created extends Reply
  final case object AlreadyExists extends Reply
  final case class Found[TData](
      data: TData
  ) extends Reply
  final case object NotFound extends Reply

  def withState[TId, TData](data: Map[TId, TData]): Behavior[Command[TId, TData]] = {
    Behaviors.receive { (context, message) =>
      message match {
        case Create(id, value, replyTo) =>
          data.get(id) match {
            case Some(value) =>
              replyTo ! AlreadyExists
              Behaviors.same
            case None =>
              replyTo ! Created
              withState(data + (id -> value))
          }

        case Read(id, replyTo) =>
          data.get(id) match {
            case Some(value) =>
              replyTo ! Found(value)
            case None =>
              replyTo ! NotFound
          }
          Behaviors.same
      }
    }
  }

  def apply[TId, TData](): Behavior[Command[TId, TData]] = withState(Map())
}
