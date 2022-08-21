package example.games.chess.models

sealed trait Action
final case class Move(from: Field, to: Field) extends Action
final case class Promote(to: MajorPieceType)
