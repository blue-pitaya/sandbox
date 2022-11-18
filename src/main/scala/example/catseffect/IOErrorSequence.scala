package example.catseffect

import cats.effect._
import cats.effect.implicits._
import cats.implicits._

object IOErrorSequence extends IOApp.Simple {

  val list = List(IO.pure(10), IO.raiseError(new NoSuchElementException()), IO.pure(12))

  override def run: IO[Unit] = for {
    l <- list.sequence
    _ <- IO.println(l)
  } yield ()
}
