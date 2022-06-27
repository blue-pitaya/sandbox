package example.akka.clustersharding

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import example.akka.general.InMemoryKeyValueRepository
import akka.actor.typed.SpawnProtocol
import akka.actor.typed.scaladsl.AskPattern._
import scala.concurrent.duration.DurationInt
import scala.concurrent.Future
import akka.actor.typed.ActorRef
import akka.util.Timeout
import akka.actor.typed.Props
import akka.cluster.sharding.typed.scaladsl.EntityTypeKey
import akka.cluster.sharding.typed.scaladsl.ClusterSharding
import akka.cluster.sharding.typed.ShardingEnvelope
import akka.cluster.sharding.typed.scaladsl.Entity

trait MainModule {
  implicit val system = ActorSystem(SpawnProtocol(), "super-system")
  implicit val ec = system.executionContext
  implicit val timeout: Timeout = 3.seconds

  private val userRepositoryBehavior = InMemoryKeyValueRepository[Int, String]()
  val userRepositoryFuture: Future[ActorRef[InMemoryKeyValueRepository.Command[Int, String]]] =
    system.ask(ref => SpawnProtocol.Spawn(userRepositoryBehavior, name = "user-repository", Props.empty, ref))

  val TypeKey = EntityTypeKey[UserCounter.Command]("CounterRegion")
  val sharding = ClusterSharding(system)
  val shardRegion: ActorRef[ShardingEnvelope[UserCounter.Command]] =
    sharding.init(
      Entity(TypeKey)(createBehavior = entityContext => UserCounter(entityContext.entityId))
    )
}
