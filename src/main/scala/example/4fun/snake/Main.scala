package example.`4fun`.snake

case class Board(width: Int, height: Int)

object Board {
  implicit val boardDrawable = new Drawable[Board] {
    override def draw(board: Board): Map[Vec2d, Char] = {
      (0 until board.width).map(Vec2d(_, 0) -> '#').toMap ++
        (0 until board.width).map(Vec2d(_, board.height - 1) -> '#').toMap ++
        (0 until board.height).map(Vec2d(0, _) -> '#').toMap ++
        (0 until board.height).map(Vec2d(board.width, _) -> '#').toMap
    }
  }
}

case class Vec2d(x: Int, y: Int)

sealed trait Drawable[A] {
  def draw(value: A): Map[Vec2d, Char]
}

object Main extends App {}
