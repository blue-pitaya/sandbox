package example.experminets

import scala.annotation.tailrec

object MultipleTailRec extends App {
  @tailrec
  def foo(x: Int): Int = x match {
    case 0 => x
    case x if x % 100 == 0 => {
      val nextX = bar(x)
      foo(nextX - 1)
    }
    case _ => foo(x - 1)
  }

  @tailrec
  def bar(x: Int, n: Int = 10): Int = n match {
    case 0 => x
    case _ => bar(x - 1, n - 1)
  }

  //will not produce stack overflow
  println(foo(1000000000))

  def f1(x: Int): Int = x match {
    case 0 => x
    case _ => f2(x - 1)
  }

  def f2(x: Int): Int = x match {
    case 0 => x
    case _ => f1(x - 1)
  }

  //will produce stack overflow
  //println(f1(1000000000))
}
