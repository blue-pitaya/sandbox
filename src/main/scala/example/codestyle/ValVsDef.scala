package example.codestyle

object Main {
  def f(x: Int): String = x.toString()
  def g(s: String): Double = 1.0
  val fg = g(f(10))
  //val fg2 = f.andThen(g) //this dont work ¯\_(ツ)_/¯

  val f1: Int => String = _.toString()
  val g1: String => Double = _ => 1.0
  val fg0 = g1(f1(10))
  val fg1 = f1 andThen g1
  val res = fg1(10)

  //compose example to complete this funny experiment
  val g2: Int => String = _.toString()
  val f2: String => Double = _ => 1.0
  val gf2: Int => Double = f2 compose g2
  val gf3: Int => Double = n => f2(g2(n))
}
