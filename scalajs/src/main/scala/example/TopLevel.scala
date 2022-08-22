package example

import scala.scalajs.js.annotation.JSExportTopLevel

object TopLevel {
  @JSExportTopLevel("incremented")
  def incremented(x: Int): Int = x + 1
}
