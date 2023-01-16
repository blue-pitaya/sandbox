package example.catseffect

import cats.effect.IOApp
import cats.effect.{ExitCode, IO}
import fs2.io.file.Watcher
import fs2.io.file.Path
import scala.concurrent.duration._

object DirectoryWatcherCancel extends IOApp {

  override def run(args: List[String]): IO[ExitCode] = {
    import cats.effect.unsafe.implicits.global

    val wat = Watcher
      .default[IO]
      .use { w =>
        for {
          cancelTask <- w.watch(Path("/home/kodus/testi"))
          _ <- w.events().evalMap(e => IO.println(e)).compile.drain
        } yield ()
      }

    for {
      fiber <- wat.start
      _ <- IO.sleep(5.seconds)
      _ <- fiber.cancel
    } yield (ExitCode.Success)
  }

}
