package example.experminets.di.cakeclass

trait MessageProviderComp { this: NumberProviderComp =>
  val messageProvider: MessageProvider
  //with implementation
  class MessageProvider {
    def message(start: String): String = s"$start with number ${numberProvider.number}"
  }
}
