package example.catseffect

import cats.effect._
import cats.syntax.all._
import scala.concurrent.duration.MILLISECONDS

object ClockExample extends IOApp {
  def measure[F[_], A](fa: F[A])(implicit F: Sync[F], clock: Clock[F]): F[(A, Long)] = {
    for {
      start <- clock.monotonic
      result <- fa
      finish <- clock.monotonic
    } yield (result, finish.toMicros - start.toMicros)
  }

  def foo: IO[Unit] = IO.blocking(println("hello world!"))

  def print[A](v: A): IO[Unit] = IO.blocking(println(v))

  override def run(args: List[String]): IO[ExitCode] = for {
    res <- measure(foo)
    time = res._2
    _ <- print(time)
  } yield (ExitCode.Success)
}
