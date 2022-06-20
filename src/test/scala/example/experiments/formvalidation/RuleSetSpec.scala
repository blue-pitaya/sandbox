package example.experiments.formvalidation

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import example.experminets.formvalidation.RuleSet._
import cats.implicits._

class RuleSetSpec extends AnyFlatSpec with Matchers {
  it should "return true when validation passes" in {
    val field = "abcde"
    val result1 = validateField(field, charsBetweenInc(4, 6))
    val result2 = validateField(field, charsBetweenInc(5, 6))
    val result3 = validateField(field, charsBetweenInc(4, 5))
    val result4 = validateField(field, charsBetweenInc(7, 8))

    result1 shouldBe true
    result2 shouldBe true
    result3 shouldBe true
    result4 shouldBe false

    val field2 = "12345"
    val result5 = validateField(field2, charsBetweenInc(3, 8) & onlyNumbers & startsWithOddDigit)

    result5 shouldBe true
  }
}
