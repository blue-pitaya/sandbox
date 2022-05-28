package example.quicks

import scala.util.Random

object HotelRoomsGenerate extends App {
  (1 to 153).foreach { hotelId =>
    val types = List("small", "medium", "large", "apartament", "studio")
    types.foreach(t => println(s"('$t', ${Random.nextInt(5)}, $hotelId),"))
  }
}
