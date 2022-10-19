package example.catseffect

import cats.effect._
import scala.concurrent.duration.DurationInt

object CancelEndedFiber extends IOApp.Simple {

  def waitAndPrintln(ms: Int): IO[Unit] = for {
    _ <- IO.sleep(ms.millis)
    _ <- IO.println(ms.toString())
  } yield ()

  override def run: IO[Unit] = for {
    fa <- waitAndPrintln(50).start
    _ <- IO.sleep(200.millis)
    // nothing happens as expected :)
    _ <- fa.cancel
  } yield ()
}
