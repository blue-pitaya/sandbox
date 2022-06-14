package example.`4fun`.snake

final case class Vec2d(x: Int, y: Int) {
  def +(o: Vec2d): Vec2d = Vec2d(x + o.x, y + o.y)
}

object Vec2d {
  implicit def ordering[A <: Vec2d]: Ordering[A] = new Ordering[A] {
    override def compare(v1: A, v2: A): Int = {
      if (v1.y > v2.y) 1
      else if (v1.y == v2.y) {
        if (v1.x > v2.x) 1 else if (v1.x == v2.x) 0 else -1
      } else -1
    }
  }
}
