package example.sandbox

object RegexMultilineWihoutMFlag extends App {
  val pattern = """(^|\n)(#)""".r
  val md = """# t
      |## t
      |### t
      |## t
      |### t
      |### t
      |## t
      |# t
      |## t
      """.stripMargin

  def extractsSeparators(source: String): List[Int] = pattern
    .findAllMatchIn(source)
    .map(m => m.start(2))
    .toList

  println(extractsSeparators(md))
  println(md.toList.map(_.toInt))
}
