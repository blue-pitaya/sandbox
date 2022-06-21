package example.experminets.formvalidation.models

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.DefaultJsonProtocol

final case class SimpleUser(name: String)

object SimpleUser extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val jsonFormat = jsonFormat1(apply)
}
