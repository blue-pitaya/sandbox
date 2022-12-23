package example

import com.raquo.laminar.api.L._
import org.scalajs.dom

object Main extends App {
  val eventBus = new EventBus[Int]

  val $numbers: Signal[List[Int]] = eventBus
    .events
    .foldLeft[List[Int]](List(1, 2, 3, 4)) { (acc, x) =>
      acc :+ x
    }

  val state = Var(List(1, 2, 3))
  val eb2 = new EventBus[Int]

  def component(): HtmlElement = {
    div(
      div(button("Add", onClick.mapTo(69) --> eventBus)),
      div(children <-- $numbers.map(ls => ls.map(x => p(x.toString()))))
    )
  }

  val containerNode = dom.document.querySelector("#app")

  render(containerNode, component())
}
