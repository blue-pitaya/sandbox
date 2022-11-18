package example.catseffect

import cats.effect._
import scala.concurrent.duration.DurationInt
import scala.concurrent.duration.FiniteDuration

object FiberIOInVar extends IOApp {

  private var saveNotemapLocallyEffect: Option[FiberIO[Unit]] = None

  private def runOrCancel(f: IO[Unit]): IO[Unit] = {
    val run = for {
      nextFib <- f.start
      _ <- IO {
        saveNotemapLocallyEffect = Some(nextFib)
      }
    } yield ()

    saveNotemapLocallyEffect match {
      case None => run
      case Some(current) => for {
          _ <- current.cancel
          _ <- run
        } yield ()
    }
  }

  private def delayedCancel(str: String, delay: FiniteDuration): IO[Unit] = for {
    _ <- IO.sleep(delay)
    _ <- IO.println(str)
  } yield ()

  override def run(args: List[String]): IO[ExitCode] = for {
    _ <- runOrCancel(delayedCancel("1", 1000.millis))
    _ <- runOrCancel(delayedCancel("2", 500.millis))
    _ <- runOrCancel(delayedCancel("3", 10.millis))
    _ <- delayedCancel("step", 20.millis)
    _ <- runOrCancel(delayedCancel("4", 10.millis))
    _ <- runOrCancel(delayedCancel("5", 20.millis))
    _ <- delayedCancel("finish", 2.seconds)
  } yield (ExitCode.Success)
}
