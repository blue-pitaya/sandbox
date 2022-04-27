package example.akka_stream_graphs

import akka.NotUsed
import akka.stream.FlowShape
import akka.stream.scaladsl._
import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import scala.concurrent.Future

object Paral extends App {
  implicit val system = ActorSystem(Behaviors.empty, "system")

  def paralellInts(flow: Flow[Int, Int, NotUsed], n: Int): Flow[Int, Int, NotUsed] =
    Flow.fromGraph(GraphDSL.create() { implicit b =>
      import GraphDSL.Implicits._

      val balance = b.add(Balance[Int](n))
      val merge = b.add(Merge[Int](n))

      for (i <- 0 until n)
        balance.out(i) ~> flow.async.async ~> merge.in(i)

      FlowShape(balance.in, merge.out)
    })

  val logAndMultiplyFlow = Flow
    .fromFunction((x: Int) => {
      println(s"started $x")
      val res = x * 2
      Thread.sleep(200)
      println(s"ended $x")
      res
    })

  val paralellFlow = paralellInts(logAndMultiplyFlow, 4)

  val source = Source(1 to 10)
  val sink = Sink.collection[Int, List[Int]]
  val ended: Future[List[Int]] = source.async.via(paralellFlow).runWith(sink)
}
