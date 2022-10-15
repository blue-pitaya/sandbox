package example.quciks

import cats.implicits._

object TraverseOption extends App {
  val list = List(Some(1), None, Some(2))
  val trav = list.traverse(identity)
  val list2 = List[Option[Int]](Some(1), Some(2))

  println(trav)
  println(list2.traverse(identity))
}
