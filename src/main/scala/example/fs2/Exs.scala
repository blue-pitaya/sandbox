package example.fs2

import fs2._
import cats.effect._
import cats.effect.unsafe.implicits.global
import fs2.io._

object Exs extends App {
  // Stream(1, 0).repeat.take(6).toList.foreach(println)
  def repeat[F[_], O](s: Stream[F, O]): Stream[F, O] = s ++ repeat(s)
  val s1 = Stream(1, 0)
  // repeat(s1).take(6).toList.foreach(println)

  Stream(1, 2, 3).drain.toList
  val s1_1 = Stream.eval(
    IO {
      println("elo")
      1
    }
  ) ++
    Stream.eval(
      IO {
        println("joł")
        2
      }
    )
  s1_1.compile.drain.unsafeRunSync()

  def drain[F[_], O](s: Stream[F, O]): Stream[F, Nothing] = s
    .repeatPull(_.uncons.flatMap(uc => Pull.pure(uc.map(_._2))))

  val s2 = Stream(1, 2, 3)
  // drain(s2).toList.foreach(println)

  val s = stdinUtf8[IO](10)
  s.take(10).map(s => println(s)).compile.drain.unsafeRunSync
}
