package example.codestyle

import scala.concurrent.Future
import akka.Done
import scala.concurrent._
import ExecutionContext.Implicits.global

object FuturePath {
  /*
   *  Fut[Boolean] => Success(v) =(v)> true  => Fut[Done]   => Success
   *                  Failure(e)       false => CustomError    Failure(e)
   */
  sealed trait CustomError
  final case object SomeCustomError extends CustomError

  //TODO: Fuctor Future[Either[_]] can be used to resolve it better
  def foo(v1: Boolean, fut2: Future[Done]): Future[Either[CustomError, Done]] = {
    val fut1 = Future(v1)
    fut1.flatMap(v => if (v) fut2.map(Right(_)) else Future(Left(SomeCustomError)))
  }
}
