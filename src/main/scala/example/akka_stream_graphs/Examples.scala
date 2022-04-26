package example.akka_stream_graphs

import akka.stream.scaladsl._
import akka.NotUsed
import akka.stream.ClosedShape
import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors

object Examples extends App {
  val source: Source[Int, NotUsed] = Source(1 to 100)
  implicit val system = ActorSystem(Behaviors.empty, "system")
  val g = RunnableGraph.fromGraph(GraphDSL.create() { implicit builder: GraphDSL.Builder[NotUsed] =>
    import GraphDSL.Implicits._
    val in = Source(1 to 10)
    val out = Sink.foreach(println)

    val bcast = builder.add(Broadcast[Int](2))
    val merge = builder.add(Merge[Int](2))

    val f1 = Flow[Int].map(_ * 10)
    val f2 = Flow[Int].map(_ + 2)
    val f4 = Flow[Int].map(_ + 1)
    val f3 = Flow[Int].map(_ + 5)

    in ~> f1 ~> bcast ~> f2 ~> merge ~> f3 ~> out
    bcast ~> f4 ~> merge
    ClosedShape
  })

  g.run()
  val f: Flow[Int, String, NotUsed] = Flow.fromFunction(n => n.toString())
  val f2 = Flow.fromFunction[Int, String](n => n.toString())
  //val f = Flow[Int, String, NotUsed].map(x => x.toString())
}
