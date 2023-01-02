package example

import com.raquo.laminar.api.L._
import org.scalajs.dom

object CombinedSignal {
  val num1 = Var(10)
  val num2 = Var(1)
  val nums = num1
    .signal
    .combineWith(num2)
    .map { case (n1, n2) =>
      n1 + n2
    }

  val out = div(child.text <-- nums)

  val component = div(
    out,
    p(child.text <-- num1),
    p(child.text <-- num2),
    button("Change 1 by 10", onClick.mapTo(num1.now() + 10) --> num1),
    button("Change 2 by 1", onClick.mapTo(num2.now() + 1) --> num2)
  )
}
