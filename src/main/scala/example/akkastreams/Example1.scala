package example.akkastreams

import akka.stream.typed._
import akka.{Done, NotUsed}
import akka.actor.typed.ActorSystem
import akka.util.ByteString
import scala.concurrent._
import scala.concurrent.duration._
import java.nio.file.Paths
import scala.io.StdIn
import akka.actor.typed.scaladsl.Behaviors
import akka.stream.scaladsl.Source
import akka.stream.IOResult
import akka.stream.scaladsl.FileIO

object Example1 extends App {
  implicit val system = ActorSystem(Behaviors.empty, "streams-example")

  // source
  val source: Source[Int, NotUsed] = Source(1 to 100)

  // consumer
  val done: Future[Done] = source.runForeach(println(_))
  implicit val ec = system.executionContext
  done.onComplete(_ => system.terminate())

  // :)

  val factorials = source.scan(BigInt(1))((acc, next) => acc * next)
  val result: Future[IOResult] =
    factorials.map(num => ByteString(s"$num\n")).runWith(FileIO.toPath(Paths.get("factorials.txt")))
}
