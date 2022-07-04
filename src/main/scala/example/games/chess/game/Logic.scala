package example.games.chess.game

import example.games.chess.models._

trait Logic {
  val nextTurn: PartialFunction[(GameState, Action), GameState]
  def terminalState(state: GameState): Boolean
  def winner(state: GameState): Option[Color]
}

object LogicImpl extends Logic {

  override val nextTurn: PartialFunction[(GameState, Action), GameState] = {
    case (GameState(board, White), Move(from, to)) if board.get(from) == Some(Piece(Pawn, White)) =>
      GameState(board, Black)
  }

  override def terminalState(state: GameState): Boolean = ???

  override def winner(state: GameState): Option[Color] = ???
}
