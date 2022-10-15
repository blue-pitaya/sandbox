package example.sandbox

object RegexEx extends App {
  val myStr = "5 678.00"
  val nums = ("""\d|\.""".r findAllIn myStr).mkString
  println(nums)
}
