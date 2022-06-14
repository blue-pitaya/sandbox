package example.`4fun`.snake

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class LogicSpec extends AnyWordSpec with Matchers {
  "nextSnake" should {
    "return empty list if no snake" in {
      val snake = List()
      val direction = Vec2d(0, 0)
      val expected = List()
      val result = Logic.nextSnakePosition(snake, direction)

      result shouldEqual expected
    }

    "move snake head in direction and rest elements should follow one before" in {
      val snake = List(Vec2d(0, 0), Vec2d(0, 1), Vec2d(2, 2))
      val direction = Vec2d(0, 1)
      val expected = List(Vec2d(0, 1), Vec2d(0, 0), Vec2d(0, 1))
      val result = Logic.nextSnakePosition(snake, direction)

      result shouldEqual expected
    }
  }
}
