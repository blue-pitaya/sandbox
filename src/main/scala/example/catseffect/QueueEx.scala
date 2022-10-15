package example.catseffect

import cats.{Contravariant, Functor}
import cats.implicits._
import cats.effect._
import cats.effect.std.{Queue, QueueSource, QueueSink}
import cats.effect.unsafe.implicits.global

object QueueEx extends App {
  def covariant(list: List[Int]): IO[List[String]] =
    (for {
      q <- Queue.bounded[IO, Int](10)
      qOfLongs: QueueSource[IO, String] = Functor[QueueSource[IO, *]].map(q)(_.toString() + "x")
      _ <- list.traverse({
        println("siemka")
        q.offer(_)
      })
      l <- List.fill(list.length)(()).traverse(_ => qOfLongs.take)
    } yield l)

  covariant(List(1, 4, 2, 3)).flatMap(IO.println(_)).unsafeRunSync()
}
