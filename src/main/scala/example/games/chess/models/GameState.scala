package example.games.chess.models

final case class GameState(board: Map[Field, Piece], turn: Color)
