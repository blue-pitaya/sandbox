package example.experminets.di.cakeclass

object Registry extends MessageProviderComp with NumberProviderComp {
  override val messageProvider: Registry.MessageProvider = new MessageProvider
  override val numberProvider: Registry.NumberProvider = new NumberProvider {
    override def number: Int = 69
  }
}
