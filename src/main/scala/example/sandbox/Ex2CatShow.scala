package example.sandbox

import cats._
import cats.implicits._

object Ex2CatShow extends App {
  implicit val catShow: Show[Cat] = Show.show { cat =>
    val name = cat.name.show
    val age = cat.age.show
    val color = cat.color.show
    s"$name is $age years-old $color cat."
  }

  println(Cat("Kolega", 420, "black").show)
}
