/*
 * Projact aims to implement simple HTTP server with functionalities:
 * 1) PUT /user/{id} -> creates new user with given id
 * 2) POST /user/{id}/inc -> increments specific user's counter
 * 3) POST /user/{id}/dec -> decrements specific user's counter
 * 4) GET /user/{id} -> return counter value
 *
 * counter is separate actor and its created per user using cluster sharding
 */
package example.akka.clustersharding

import akka.actor.typed.ActorRef
import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.AskPattern._
import akka.actor.typed.scaladsl.Behaviors
import akka.cluster.sharding.typed.ShardingEnvelope
import akka.cluster.sharding.typed.scaladsl.ClusterSharding
import akka.cluster.sharding.typed.scaladsl.Entity
import akka.cluster.sharding.typed.scaladsl.EntityRef
import akka.cluster.sharding.typed.scaladsl.EntityTypeKey
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.model.StatusCodes.ClientError
import akka.http.scaladsl.model.StatusCodes.CustomStatusCode
import akka.http.scaladsl.model.StatusCodes.Informational
import akka.http.scaladsl.model.StatusCodes.Redirection
import akka.http.scaladsl.model.StatusCodes.ServerError
import akka.http.scaladsl.model.StatusCodes.Success
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.util.Timeout
import example.akka.general.InMemoryKeyValueRepository._

import scala.concurrent.Future
import scala.io.StdIn
import scala.util.Failure
import scala.util.Success

object ShardingMain extends App with MainModule {
  private def createUser(id: Int) = {
    userRepositoryFuture.flatMap { repo =>
      repo.ask(ref => Create[Int, String](id, s"user-counter-$id", ref))
    }
  }

  private def updateCounter(id: Int, cmd: UserCounter.Command) = {
    userRepositoryFuture
      .flatMap { repo =>
        repo.ask(ref => Read[Int, String](id, ref))
      }
      .map { res =>
        res match {
          case Found(userCounterId) =>
            //TODO: userCounterId is String but its need to be proven by types
            shardRegion ! ShardingEnvelope(userCounterId.asInstanceOf[String], cmd)
          case _ =>
        }
        res
      }
  }

  //Option[Int] hides potential errors but who cares?
  private def getCounter(id: Int): Future[Option[Int]] =
    userRepositoryFuture
      .flatMap { repo =>
        repo.ask(ref => Read[Int, String](id, ref))
      }
      .flatMap { res =>
        res match {
          case Found(userCounterId) =>
            shardRegion
              .ask[UserCounter.CounterReply](ref =>
                ShardingEnvelope(userCounterId.asInstanceOf[String], UserCounter.GetValue(ref))
              )
              .map(reply => Some(reply.value))
          case _ => Future.successful(None)
        }
      }

  private def updateCounterPath(id: Int, cmd: UserCounter.Command) =
    onSuccess(updateCounter(id, cmd)) { res =>
      res match {
        case Found(data) => complete("ok")
        case NotFound    => complete("not found")
        case _           => complete(500, "error")
      }
    }

  private def userRoute: Route = path("user" / IntNumber) { id =>
    put {
      onSuccess(createUser(id)) { res =>
        res match {
          case Created       => complete("ok")
          case AlreadyExists => complete("already exists")
          case _             => complete(500, "error") //TODO: catch exception better
        }
      }
    } ~
      get {
        onSuccess(getCounter(id)) { res =>
          res match {
            case Some(value) => complete(value.toString())
            case None        => complete("not found")
          }
        }
      }
  }

  private def counterRoute: Route = path("user" / IntNumber / "inc") { id =>
    post { updateCounterPath(id, UserCounter.Increment) }
  } ~
    path("user" / IntNumber / "dec") { id =>
      post { updateCounterPath(id, UserCounter.Decrement) }
    }

  def mainRoute = userRoute ~ counterRoute
  val bindingFuture = Http().newServerAt("localhost", 3169).bind(mainRoute)
  StdIn.readLine()

  bindingFuture.flatMap(_.unbind()).onComplete(_ => system.terminate())

  //val counter1Ref: EntityRef[UserCounter.Command] = sharding.entityRefFor(TypeKey, "counter-1")
  //counter1Ref ! UserCounter.Increment
  //counter1Ref ! UserCounter.Increment
  //shardRegion ! ShardingEnvelope("counter-2", UserCounter.Increment)

  //val counterValueFuture = counter1Ref.ask(ref => UserCounter.GetValue(ref))
  //counterValueFuture.onComplete { x =>
  //  x match {
  //    case Failure(exception) => println(exception)
  //    case Success(value)     => system.log.info(value.value.toString())
  //  }
  //}

  //val counter2Ref: EntityRef[UserCounter.Command] = sharding.entityRefFor(TypeKey, "counter-2")
  //val counterValueFuture2 = counter2Ref.ask(ref => UserCounter.GetValue(ref))
  //counterValueFuture2.onComplete { x =>
  //  x match {
  //    case Failure(exception) => println(exception)
  //    case Success(value)     => system.log.info(value.value.toString())
  //  }
  //}
}
