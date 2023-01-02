package example

import com.raquo.laminar.api.L._
import org.scalajs.dom

object ToggleComponent {
  val label1 = p("Oh hi mark.")
  val label2 = p("Huh?")

  val showLabel1 = Var(true)

  val labelSignal = showLabel1
    .signal
    .map { x =>
      if (x) label1
      else label2
    }

  def component =
    div(child <-- labelSignal, button("toggle", onClick.mapTo(!showLabel1.now()) --> showLabel1))
}

object ToggleComponentEnum {
  sealed trait View
  final case object View1 extends View
  final case object View2 extends View
  final case object View3 extends View

  lazy val p1 = p("1")
  lazy val p2 = p("2")
  lazy val p3 = p("3")

  val currentView = Var[View](View1)
  val $view = currentView
    .signal
    .map {
      case View1 => p1
      case View2 => p2
      case View3 => p3
    }

  def nextView(current: View): View = current match {
    case View1 => View2
    case View2 => View3
    case View3 => View1
  }

  def component = div(
    child <-- $view,
    button("toggle", onClick.mapTo(nextView(currentView.now())) --> currentView)
  )
}
