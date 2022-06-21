package example.experminets.formvalidation

import akka.http.scaladsl.model._
import akka.http.scaladsl.server._
import akka.http.scaladsl.server.Directives._
import example.experminets.formvalidation.models.SimpleUser

object Routes {
  val textParam: Directive1[String] =
    parameter("text".as[String])

  val lengthDirective: Directive1[Int] =
    textParam.map(text => text.length)

  def validateData(user: SimpleUser): Directive0 = Directive { inner => ctx =>
    if (user.name.length() > 4) {
      ctx.complete("bad")
    } else
      inner()(ctx)
  }

  val main = path("ok") {
    get {
      validateData(SimpleUser("user")) { ctx =>
        ctx.complete("ok")
      }
    }
  } ~
    path("bad") {
      get {
        validateData(SimpleUser("userBAD")) {
          complete("ok")
        }
      }
    }
}
