package example.experminets.di.cakeclass

trait NumberProviderComp {
  val numberProvider: NumberProvider
  //without implementation
  trait NumberProvider {
    def number: Int
  }
}
