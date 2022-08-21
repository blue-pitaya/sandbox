package example.codestyle

object OptionResolve {
  def printVal(v: Int): String = s"$v is ok"

  def printErr(): String = s"no value"

  def patternMatchingMethod(maybeVal: Option[Int]): String = {
    maybeVal match {
      case Some(value) => printVal(value)
      case None        => printErr()
    }
  }

  def mapMethod(maybeVal: Option[Int]): String = maybeVal.map(printVal(_)).getOrElse(printErr())
}
