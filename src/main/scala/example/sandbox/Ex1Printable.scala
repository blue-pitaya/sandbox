package example.sandbox

trait Printable[A] {
  def format(value: A): String
}

object Printable {
  def format[A](value: A)(implicit p: Printable[A]): String = p.format(value)

  def print[A](value: A)(implicit p: Printable[A]): Unit = println(format(value))
}

object PrintableInstances {
  implicit val intPrintable = new Printable[Int] {
    override def format(value: Int): String = value.toString
  }

  implicit val stringPrintable = new Printable[String] {
    override def format(value: String): String = value
  }
}

final case class Cat(name: String, age: Int, color: String)

object Cat {
  import PrintableInstances._

  implicit val catPrintable = new Printable[Cat] {
    override def format(value: Cat): String =
      s"${Printable.format(value.name)} ia a ${Printable.format(value.age)} years-old ${Printable.format(value.color)} cat."
  }
}

object PrintableSyntax {
  implicit class PrintableOps[A](value: A) {
    def format(implicit p: Printable[A]): String = p.format(value)

    def print(implicit p: Printable[A]): Unit = println(format(p))
  }
}

object Ex1Printable extends App {
  //val n = 10
  //val s = "elo"

  //import PrintableInstances._
  //val formattedN = Printable.format(n)
  //val formattedS = Printable.format(s)

  val cat = Cat("Little motherfucker", 69, "transparent")
  Printable.print(cat)

  import PrintableSyntax.PrintableOps
  cat.print
}
