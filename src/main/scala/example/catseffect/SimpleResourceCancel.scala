package example.catseffect

import cats.effect.IOApp
import cats.effect.{ExitCode, IO}
import fs2.io.file.Watcher
import fs2.io.file.Path
import scala.concurrent.duration._
import cats.effect.kernel.Resource

object SimpleResourceCancel extends IOApp {

  override def run(args: List[String]): IO[ExitCode] = {
    import cats.effect.unsafe.implicits.global

    val res = Resource.make[IO, Unit](IO.println("start"))(_ => IO.println("end"))

    val useRes = res.use { _ =>
      for {
        _ <- IO.sleep(1.second)
      } yield ()
    }

    for {
      fiber <- useRes.start
      _ <- IO.sleep(5.millis)
      _ <- fiber.cancel
    } yield (ExitCode.Success)
  }

}
