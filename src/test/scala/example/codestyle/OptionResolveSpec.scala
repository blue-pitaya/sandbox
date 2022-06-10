package example.codestyle

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class OptionResolveSpec extends AnyFlatSpec with Matchers {
  "some int" should "return valid msg" in {
    val value: Option[Int] = Some(69)
    val expected1 = OptionResolve.patternMatchingMethod(value)
    val expected2 = OptionResolve.mapMethod(value)

    expected1 shouldBe "69 is ok"
    expected2 shouldBe "69 is ok"
  }

  "no value" should "return error msg" in {
    val value: Option[Int] = None
    val expected1 = OptionResolve.patternMatchingMethod(value)
    val expected2 = OptionResolve.mapMethod(value)

    expected1 shouldBe "no value"
    expected2 shouldBe "no value"
  }
}
