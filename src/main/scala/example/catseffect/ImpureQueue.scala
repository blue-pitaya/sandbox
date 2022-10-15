package example.catseffect

import scala.io.StdIn
import cats.effect._
import cats.effect.std._
import fs2._
import cats.effect.unsafe.implicits.global

object ImpureQueue extends App {
  var line = "empty"
  var listener: String => IO[Unit] = _ => IO.unit

  def loop(): Unit = {
    line = StdIn.readLine()
    listener(line).unsafeRunAndForget()

    if (line == "q") ()
    else loop()
  }

  def start(q: Queue[IO, String]): Stream[IO, Unit] = Stream
    .repeatEval(q.take)
    .foreach(s => IO.println(s))

  val program = for {
    queue <- Queue.unbounded[IO, String]
    _ <- queue.offer("xd")
    // stream = Stream.fromQueueNoneTerminated(queue)
    _ = {
      listener = s => queue.offer(s)
    }
    s <- start(queue).compile.drain
  } yield (s)

  // val done = async.signalOf(false).unsafeRun

  program.unsafeRunAndForget()

  loop()

  // val s = Stream.eval(
  //  Queue
  //    .unbounded[IO, String]
  //    .flatTap { q =>
  //      IO.delay {
  //        println("dupsko")
  //        listener = s => {
  //          println("pizda")
  //          q.offer(s)
  //        }
  //      }
  //    }
  // )

  // val x = s
  //  .map(q =>
  //    q.take
  //      .map(i => {
  //        println("xd")
  //        i
  //      })
  //  )
  //  .compile
  //  .drain

  // x.unsafeRunAndForget()

  // loop()

  // abstract class ImpureInterface {
  //  def onMessage(msg: String): Unit

  //  def init(): Unit = {
  //    onMessage(q)
  //  }

  //  // ...
  // }

  // Dispatcher[IO].use { dispatcher =>
  //  for {
  //    queue <- Queue.unbounded[IO, String]
  //    impureInterface <- IO.delay {
  //      new ImpureInterface {
  //        override def onMessage(msg: String): Unit = dispatcher.unsafeRunSync(queue.offer(msg))
  //      }
  //    }
  //    _ <- IO.delay(impureInterface.init())
  //    value <- queue.tryTake
  //  } yield value match {
  //    case Some(v) => println(s"Value found in queue! $v")
  //    case None    => println("Value not found in queue :(")
  //  }
  // }
}
