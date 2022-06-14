package example.`4fun`.snake

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class RendererSpec extends AnyWordSpec with Matchers {
  "toCharMap" should {
    "return empty map when text is empty" in {
      val txt = ""
      val expected = Map[Vec2d, Char]()

      val result1 = Renderer.toCharMap1(txt)
      val result2 = Renderer.toCharMap2(txt)

      result1 shouldEqual expected
      result2 shouldEqual expected
    }

    "return map without no whitespace chars" in {
      val txt = "#  #\n#  #"
      val expected = Map[Vec2d, Char](Vec2d(0, 0) -> '#', Vec2d(3, 0) -> '#', Vec2d(0, 1) -> '#', Vec2d(3, 1) -> '#')

      val result1 = Renderer.toCharMap1(txt)
      val result2 = Renderer.toCharMap2(txt)

      result1 shouldEqual expected
      result2 shouldEqual expected
    }
  }

  "toText" should {
    "return empty string when map is empty" in {
      val charMap = Map[Vec2d, Char]()
      val expected = ""
      val result = Renderer.toText(charMap)

      result shouldEqual expected
    }

    "ignore chars on negative axies" in {
      val charMap = Map(Vec2d(-2, -1) -> 'a', Vec2d(2, 2) -> 'b')
      val expected = """
      |
      |
      |  b
      """.stripMargin.trim()
      val result = Renderer.toText(charMap)

      result shouldEqual expected
    }

    "return text with necessary whitespaces and newlines" in {
      val charMap = Map[Vec2d, Char](Vec2d(2, 3) -> 'o', Vec2d(3, 3) -> 'a', Vec2d(5, 5) -> 'b')
      val expected = """
      |
      |
      |
      |  oa
      |
      |      b
      """.stripMargin.trim()
      val result = Renderer.toText(charMap)

      result shouldEqual expected
    }
  }
}
