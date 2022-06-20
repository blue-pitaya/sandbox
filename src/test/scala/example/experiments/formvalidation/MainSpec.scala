package example.experiments.formvalidation

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import example.experminets.formvalidation.User
import example.experminets.formvalidation.Main

class MainSpec extends AnyFlatSpec with Matchers {
  it should "return empty map if user is valid" in {
    val user = User("abc", "pass12", 20)
    val result = Main.validateUser(user)

    result shouldEqual Map()
  }

  it should "return errors when login is too short and not letters" in {
    val user = User("1", "pass12", 20)
    val result = Main.validateUser(user)

    result shouldEqual Map("login" -> List("length must be > 3", "value must be only letters"))
  }
}
