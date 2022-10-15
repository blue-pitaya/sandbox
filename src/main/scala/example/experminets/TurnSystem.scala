package example

import cats._
import cats.data._

trait Turnable[A] {
  def baseSpeed(value: A): Int
}

case class PlayerInfo(name: String, baseSpeed: Int)

object PlayerInfo {
  implicit val playerTurnable = new Turnable[PlayerInfo] {
    override def baseSpeed(value: PlayerInfo): Int = value.baseSpeed
  }
}

object TurnLogic {
  case class Pointed[A](e: A, points: Int)

  private def speedPoints[A](e: A)(implicit t: Turnable[A]): Int = 100 - t.baseSpeed(e)

  private def _current[A](es: NonEmptyList[Pointed[A]], turn: Int)(implicit t: Turnable[A]): A = {
    val curr = Reducible[NonEmptyList].reduce(es)((s, i) => if (s.points < i.points) s else i)
    turn match {
      case 0 => curr.e
      case _ =>
        val nextEs = es.map(e => if (e == curr) e.copy(points = e.points + speedPoints(e.e)) else e)
        _current(nextEs, turn - 1)
    }
  }

  def current[A](es: NonEmptyList[A], turn: Int)(implicit t: Turnable[A]): A = _current(es.map(Pointed(_, 0)), turn)

  def next[A](es: NonEmptyList[Pointed[A]])(implicit t: Turnable[A]): NonEmptyList[Pointed[A]] = {
    val curr = Reducible[NonEmptyList].reduce(es)((s, i) => if (s.points < i.points) s else i)
    return es.map(e => if (e == curr) e.copy(points = e.points + speedPoints(e.e)) else e)
  }
}
