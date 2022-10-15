package example.sandbox

import cats._
import cats.implicits._

object Ex3Eq extends App {
  implicit val catEq = Eq.instance[Cat] { (c1, c2) =>
    c1.name === c2.name && c1.age === c2.age && c1.color === c2.color
  }

  val cat1 = Cat("Garfield", 38, "orange and black")
  val cat2 = Cat("Heathcliff", 33, "orange and black")

  println(cat1 === cat2)

  val optionCat1 = cat1.some
  val optionCat2 = none[Cat]

  println(optionCat1 =!= optionCat2)
}
