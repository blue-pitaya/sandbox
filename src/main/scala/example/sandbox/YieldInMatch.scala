package example.sandbox

object YieldInMatch extends App {
  val maybeInt: Option[Int] = Some(11)
  for {
    n <- maybeInt
  } yield (n match {
    case 12 => println("wont be")
    case _  => println("this line prevents match exception")
  })

  //returning option
  //method 1
  def parse(str: String): Option[Int] = {
    str match {
      case "some"  => Some(0)
      case "other" => Some(1)
      case _       => None
    }
  }

  //method 2
  def parse2(str: String): Option[Int] = {
    val matcher: PartialFunction[String, Int] = {
      case "some"  => 0
      case "other" => 1
    }
    matcher.lift(str)
  }
}
