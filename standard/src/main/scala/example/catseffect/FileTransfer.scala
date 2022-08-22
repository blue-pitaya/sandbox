package example.catseffect

import cats.effect.ExitCode
import cats.effect._
import cats.syntax.all._
import cats._

import java.io._
import scala.io.StdIn

object FileTransfer extends IOApp {
  def inputStream[F[_]: Sync](f: File): Resource[F, FileInputStream] =
    Resource.make {
      Sync[F].blocking(new FileInputStream(f))
    } { inStream => Sync[F].blocking(inStream.close()).handleErrorWith(_ => Sync[F].unit) }

  def outputStream[F[_]: Sync](f: File): Resource[F, FileOutputStream] =
    Resource.make {
      Sync[F].blocking(new FileOutputStream(f))
    } { outStream => Sync[F].blocking(outStream.close()).handleErrorWith(_ => Sync[F].unit) }

  def inputOutputStream[F[_]: Sync](in: File, out: File): Resource[F, (FileInputStream, FileOutputStream)] =
    for {
      inStream <- inputStream(in)
      outStream <- outputStream(out)
    } yield (inStream, outStream)

  def transmit[F[_]: Sync](origin: InputStream, destination: OutputStream, buffer: Array[Byte], acc: Long): F[Long] =
    for {
      amount <- Sync[F].blocking(origin.read(buffer, 0, buffer.size))
      count <-
        if (amount > -1)
          Sync[F].blocking(destination.write(buffer, 0, amount)) >> transmit(origin, destination, buffer, acc + amount)
        else Sync[F].pure(acc)
    } yield count

  def transfer[F[_]: Sync](origin: InputStream, destination: OutputStream, bufferSize: Int): F[Long] =
    transmit(origin, destination, new Array[Byte](bufferSize), 0L)

  def askForOverwriting[F[_]: Sync](): F[Unit] = for {
    _ <- Sync[F].blocking(print("Destionation file exist. Do you want to overwrite it? (y/n): "))
    input <- Sync[F].blocking(StdIn.readLine())
    _ <- if (input == "y") Sync[F].unit else Sync[F].raiseError(new Exception("Aborted"))
  } yield ()

  def copy[F[_]: Sync](origin: File, destination: File, bufferSize: Int): F[Long] =
    inputOutputStream(origin, destination).use { case (in, out) =>
      for {
        _ <-
          if (bufferSize <= 0) Sync[F].raiseError(new IllegalArgumentException("Buffer size must be > 0!"))
          else Sync[F].unit
        _ <- if (destination.exists()) askForOverwriting() else Sync[F].unit
        count <- transfer(in, out, bufferSize)
      } yield (count)
    }

  val defaultBufferSize = 1024 * 10

  override def run(args: List[String]): IO[ExitCode] = (for {
    _ <- if (args.length < 2) IO.raiseError(new IllegalArgumentException("Need two files to run!")) else IO.unit
    orig = new File(args(0))
    dest = new File(args(1))
    bufferSize = args.lift(2).flatMap(_.toIntOption).getOrElse(defaultBufferSize)
    _ <- if (orig == dest) IO.raiseError(new IllegalArgumentException("Two same files!")) else IO.unit
    count <- copy[IO](orig, dest, bufferSize)
    _ <- IO.println(s"$count bytes copied from $orig to $dest")
  } yield ExitCode.Success).handleErrorWith(e =>
    IO.blocking(println(s"Error: ${e.getMessage()}")) >> IO.pure(ExitCode.Error)
  )
}
