package example.catseffect

import cats.effect._
import cats.syntax.all._
import scala.concurrent.duration.DurationInt

object TemporalExample extends IOApp {
  def setTimeout[F[_], A](fa: F[A])(implicit F: Async[F]): F[A] = Temporal[F].sleep(2.seconds) >> fa

  def hello = IO.blocking(println("hello world!"))

  override def run(args: List[String]): IO[ExitCode] = for {
    _ <- setTimeout(hello)
  } yield (ExitCode.Success)
}
