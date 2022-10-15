package example.catseffect

import cats.effect._
import scala.concurrent.duration.FiniteDuration
import scala.concurrent.duration.DurationInt
import cats.effect.unsafe.implicits.global

object RunNTimesExample extends App {
  //original was: js.Function1[Int, Unit]
  def run5TimesWith1SecondInterval(fn: Int => Unit) {

    def runAtLeastTimes[A](fa: IO[A], n: Int, interval: FiniteDuration)(implicit
        F: Async[IO]
    ): IO[A] = {
      def delayedFa = Temporal[IO].sleep(interval) >> fa

      (0 until n - 1).foldLeft(delayedFa) { (acc, _) => acc >> delayedFa }
    }

    def incrementCounter(ref: Ref[IO, Int])(implicit F: Sync[IO]): IO[Int] =
      for {
        v <- ref.get
        incremented <- ref.modify(x => (x + 1, x))
      } yield (incremented)

    def onTick(fa: IO[Int]) = for {
      v <- fa
      _ <- IO.blocking(fn(v))
    } yield (v)

    def run = for {
      v <- Ref[IO].of(0)
      res <- runAtLeastTimes(onTick(incrementCounter(v)), 5, 1.second)
    } yield ()

    run.unsafeRunAndForget()
  }

  run5TimesWith1SecondInterval(println(_))
}
