package example.games.chess.game

import example.games.chess.models._

object CanMove {
  //all
  def numbersBetween(a: Int, b: Int): Seq[Int] =
    if (a < b) (a + 1 until b)
    else if (b < a) (b + 1 until a)
    else Seq()

  def canTakeOrFinalMove(field: Field, color: Color, board: Map[Field, Piece]): Boolean =
    board.get(field).map(p => p.color == color.opposite).getOrElse(true)

  //pawn
  def canPawnDoubleMove(file: Char, fromRank: Int, toRank: Int, color: Color, pieceExist: (Field) => Boolean): Boolean =
    (fromRank, toRank, color) match {
      case (2, 3, White) => !pieceExist(Field(file, 3))
      case (2, 4, White) => !pieceExist(Field(file, 3)) && !pieceExist(Field(file, 4))
      case (7, 6, Black) => !pieceExist(Field(file, 6))
      case (7, 5, Black) => !pieceExist(Field(file, 6)) && !pieceExist(Field(file, 7))
      case _             => false
    }
  //TODO:

  //rook
  def canRookMove(from: Field, to: Field, color: Color, board: Map[Field, Piece]): Boolean = {
    val pieceExist = (f: Field) => board.get(f).isDefined
    lazy val canMoveAlmost =
      if (from.file == to.file) !numbersBetween(from.rank, to.rank).exists(r => pieceExist(Field(from.file, r)))
      else if (from.rank == to.rank) !numbersBetween(from.file, to.file).exists(r => pieceExist(Field(r, from.rank)))
      else false

    lazy val canFinalMove = canTakeOrFinalMove(to, color, board)

    canMoveAlmost && canFinalMove
  }

  //bishop
  def canBishopMove(from: Field, to: Field, color: Color, board: Map[Field, Piece]): Boolean = {
    val pieceExist = (f: Field) => board.get(f).isDefined
    lazy val isOnDiag = Math.abs(from.rank - to.rank) == Math.abs(from.file - to.file)
    lazy val piecesOnWay = {
      val fieldsBetween = numbersBetween(from.file, to.file) zip numbersBetween(from.rank, to.rank) map { pos =>
        Field(pos._1, pos._2)
      }
      fieldsBetween.exists(f => pieceExist(f))
    }
    lazy val canFinalMove = canTakeOrFinalMove(to, color, board)

    isOnDiag && !piecesOnWay && canFinalMove
  }

  //knight
  def canKnightMove(from: Field, to: Field, color: Color, board: Map[Field, Piece]): Boolean = {
    val xDist = Math.abs(from.file - to.file)
    val yDist = Math.abs(from.rank - to.rank)
    lazy val isLegalMove = (xDist == 1 && yDist == 2) || (xDist == 2 && yDist == 1)
    lazy val canFinalMove = canTakeOrFinalMove(to, color, board)

    isLegalMove && canFinalMove
  }

  //queen
  def canQueenMove(from: Field, to: Field, color: Color, board: Map[Field, Piece]): Boolean =
    canRookMove(from, to, color, board) || canBishopMove(from, to, color, board)

}
