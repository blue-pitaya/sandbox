package example.experiments.di.cakeclass

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import example.experminets.di.cakeclass.NumberProviderComp
import example.experminets.di.cakeclass.MessageProviderComp
import org.scalamock.scalatest.MockFactory

object TestRegistry extends NumberProviderComp with MessageProviderComp with MockFactory {
  override val numberProvider: NumberProvider = mock[NumberProvider]
  override val messageProvider: MessageProvider = new MessageProvider
}

//TODO:
class MessageProviderSpec extends AnyFlatSpec with Matchers {
  "message" must "be ok" in {
    //(TestRegistry.numberProvider.number _).expects().returning(420)
    TestRegistry.messageProvider.message("robot bitch") shouldEqual "robot bitch with number 420"
  }
}
