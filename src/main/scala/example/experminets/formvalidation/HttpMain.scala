package example.experminets.formvalidation

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import scala.io.StdIn

object HttpMain extends App {
  implicit val system = ActorSystem(Behaviors.empty, "validation-route")
  implicit val ec = system.executionContext

  val bindingFuture = Http().newServerAt("localhost", 3010).bind(Routes.main)

  StdIn.readLine();

  bindingFuture
    .flatMap(_.unbind())
    .onComplete(_ => system.terminate())

}
