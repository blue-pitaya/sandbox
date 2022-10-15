package example.experminets.onlymultipletraits

trait NumberComponent {
  val db: Int
}

trait NumberComponentImpl extends NumberComponent {
  override val db: Int = 69
}
