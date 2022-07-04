package example.games.snake

import scala.collection.immutable

object Logic {
  def nextSnakePosition(snake: List[Vec2d], direction: Vec2d): List[Vec2d] = {
    snake match {
      case head :: next  => (head + direction) :: snake.dropRight(1)
      case immutable.Nil => snake
    }
  }

  def nextTick(state: GameState): GameState = {
    val nextSnake = nextSnakePosition(state.snake, state.snakeDirection)

    state
  }
}
