package example.experminets.onlymultipletraits

trait FartComponent {
  val fartingForLife: String
  def giveMeHugeFart(): String
}

trait FartComponentImpl extends FartComponent { this: NumberComponent =>
  override val fartingForLife: String = "i love farting in the morning"
  override def giveMeHugeFart(): String = s"$fartingForLife and i like number $db"
}
