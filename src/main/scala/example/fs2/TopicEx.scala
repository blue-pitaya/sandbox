package example.fs2

import cats.effect._
import cats.effect.unsafe.implicits.global
import fs2._
import fs2.concurrent._

object TopicEx extends App {
  Topic[IO, String]
    .flatMap { topic =>
      val publisher = Stream.constant("1").covary[IO].through(topic.publish)
      val subscriber = topic.subscribe(10).take(20)
      subscriber.concurrently(publisher).foreach(s => IO.println(s)).compile.drain
    }
    .unsafeRunSync()
}
