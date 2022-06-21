package example.experiments.formvalidation

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.http.scaladsl.model.StatusCodes
import example.experminets.formvalidation.Routes

class RoutesSpec extends AnyFlatSpec with Matchers with ScalatestRouteTest {
  "/ok" should "return ok response" in {
    Get("/ok") ~> Routes.main ~> check {
      status shouldEqual StatusCodes.OK
      responseAs[String] shouldEqual "ok"
    }
  }

  "/bad" should "return bad response" in {
    Get("/bad") ~> Routes.main ~> check {
      status shouldEqual StatusCodes.OK
      responseAs[String] shouldEqual "bad"
    }
  }
}
