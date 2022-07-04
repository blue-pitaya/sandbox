package example.games.chess.models

sealed trait PieceType
sealed trait MajorPieceType extends PieceType
final case object Pawn extends PieceType
final case object Rook extends MajorPieceType
final case object Bishop extends MajorPieceType
final case object Knight extends MajorPieceType
final case object Queen extends MajorPieceType
final case object King extends PieceType
