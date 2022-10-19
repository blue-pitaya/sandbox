package example.catseffect

import cats.effect._
import scala.concurrent.duration.DurationInt

object CancellableSingleRunFiber extends IOApp {

  def waitAndPrintln(ms: Int): IO[Unit] = for {
    _ <- IO.sleep(ms.millis)
    _ <- IO.println(ms.toString())
  } yield ()

  // def x = for {
  //  f1 <- waitAndPrintln(1000).start
  //  f2 <- waitAndPrintln(10).start
  //  f3 <- waitAndPrintln(2000).start
  //  _ <- f3.cancel
  //  _ <- f1.join
  //  _ <- f2.join
  // } yield ()

  def runOrCancel(fa: IO[Unit], ref: Ref[IO, Option[FiberIO[Unit]]]) = for {
    current <- ref.get
    // cancel previous if exists
    _ <- current match {
      case None        => IO.unit
      case Some(value) => value.cancel
    }
    nextFib <- fa.start
    _ <- ref.modify(x => (Some(nextFib), x))
  } yield ()

  override def run(args: List[String]): IO[ExitCode] = for {
    ref <- Ref.of[IO, Option[FiberIO[Unit]]](None)
    _ <- runOrCancel(waitAndPrintln(1000), ref)
    _ <- runOrCancel(waitAndPrintln(2000), ref)
    _ <- runOrCancel(waitAndPrintln(10), ref)
    _ <- runOrCancel(waitAndPrintln(500), ref)
    _ <- IO.sleep(2000.millis)
  } yield (ExitCode.Success)
}
