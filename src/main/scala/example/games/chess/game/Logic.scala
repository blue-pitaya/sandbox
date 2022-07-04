package example.games.chess.game

import example.games.chess.models.GameState
import example.games.chess.models.Action
import example.games.chess.models.Color

trait Logic {
  val nextTurn: PartialFunction[(GameState, Action), GameState]
  def terminalState(state: GameState): Boolean
  def winner(state: GameState): Option[Color]
}

object LogicImpl extends Logic {
  override val nextTurn: PartialFunction[(GameState, Action), GameState] = ???

  override def terminalState(state: GameState): Boolean = ???

  override def winner(state: GameState): Option[Color] = ???
}
