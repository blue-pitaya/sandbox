package example.experminets.onlymultipletraits

trait FunkyComponent {
  val vibe: String
}

trait FunkyComponentImpl extends FunkyComponent {
  override val vibe: String = "release your funky spirit"
}
