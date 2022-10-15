package example.games.chess.models

sealed trait Color {
  def opposite: Color
}
final case object White extends Color {
  override def opposite: Color = Black
}
final case object Black extends Color {
  override def opposite: Color = White
}
