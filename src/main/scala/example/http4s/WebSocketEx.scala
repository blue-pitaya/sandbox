package example.http4s

import sttp.tapir._
import sttp.tapir.server.http4s.Http4sServerInterpreter
import org.http4s.HttpRoutes
import cats.effect._
import org.http4s.blaze.server.BlazeServerBuilder
import fs2._
import org.http4s.server.websocket.WebSocketBuilder2
import scala.concurrent.ExecutionContext
import sttp.capabilities.fs2.Fs2Streams
import sttp.capabilities.WebSockets
import org.http4s.server.Router
import scala.concurrent.duration._

// An infinite stream of the periodic elapsed time

object WebSocketEx extends IOApp {

  def countCharacters(s: String): IO[Either[Unit, Int]] = IO.pure(Right[Unit, Int](s.length))

  val countCharactersEndpoint: PublicEndpoint[String, Unit, Int, Any] = endpoint
    .in(stringBody)
    .out(plainBody[Int])

  val countCharactersRoutes: HttpRoutes[IO] = Http4sServerInterpreter[IO]()
    .toRoutes(countCharactersEndpoint.serverLogic(countCharacters _))

  implicit val ec: ExecutionContext = scala.concurrent.ExecutionContext.Implicits.global

  val wsEndpoint
      : PublicEndpoint[Unit, Unit, Pipe[IO, String, String], Fs2Streams[IO] with WebSockets] =
    endpoint
      .get
      .in("count")
      .out(
        webSocketBody[String, CodecFormat.TextPlain, String, CodecFormat.TextPlain](Fs2Streams[IO])
      )

  val stream: Pipe[IO, String, String] =
    stream => Stream.awakeEvery[IO](1.second).map(_ => "dupsko")

  val wsRoutes: WebSocketBuilder2[IO] => HttpRoutes[IO] = Http4sServerInterpreter[IO]()
    .toWebSocketRoutes(wsEndpoint.serverLogicSuccess[IO](_ => IO.pure(stream)))

  val xd = BlazeServerBuilder[IO]
    .withExecutionContext(ec)
    .bindHttp(8080, "127.0.0.1")
    .withHttpWebSocketApp(wsb => Router("/" -> wsRoutes(wsb)).orNotFound)
    .serve

  override def run(args: List[String]): IO[ExitCode] = xd.compile.drain.as(ExitCode.Success)
}
