package example.catseffect

import cats.effect._
import scala.concurrent.duration.FiniteDuration
import scala.concurrent.duration.DurationInt
import cats.effect.unsafe.implicits.global
import cats.data.OptionT
import cats.Monad

object RunUntil extends IOApp {

  case class State(n: Int)

  def next(s: State): Option[State] = {
    val next = s.n + 1
    if (next > 3) None else Some(s.copy(n = next))
  }

  def delayedRun(state: State)(implicit F: Async[IO]): IO[Option[State]] =
    Temporal[IO].sleep(1.second) >> IO.blocking(next(state))

  def callbackRun(state: State, callBk: State => Unit): IO[Option[State]] = (for {
    next <- OptionT(delayedRun(state))
    _ <- OptionT.liftF(IO.blocking(callBk(next)))
  } yield (next)).value

  def callbackRunRef(state: Ref[IO, State], callBk: State => Unit) = (for {
    value <- OptionT.liftF(state.get)
    next <- OptionT(callbackRun(value, callBk))
    _ <- OptionT.liftF(state.modify(s => (next, value)))
  } yield ()).value

  def runUntil(state: State, callBk: State => Unit) = {
    val ref = Ref[IO].of(state)
    for {
      r <- ref
      _ <- Monad[IO].iterateUntil(callbackRunRef(r, callBk))(!_.isDefined)
    } yield ()
  }

  override def run(args: List[String]): IO[ExitCode] = for {
    _ <- runUntil(State(0), s => println(s.n))
  } yield (ExitCode.Success)
}
