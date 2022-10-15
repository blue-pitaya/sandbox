package example.akka_stream_graphs

import akka.stream.scaladsl.RunnableGraph
import akka.stream.scaladsl.GraphDSL
import akka.stream.ClosedShape
import akka.stream.scaladsl._
import akka.NotUsed
import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors

final case class Link(v: String)
final case class Page(v: String)
final case class Data(v: String)

object Scraper extends App {
  implicit val system = ActorSystem(Behaviors.empty, "system")

  def getLinks(page: Page): List[String] = {
    page.v.split(' ').filter(_.startsWith("#")).toList
  }

  def getData(page: Page): List[String] = {
    page.v.split(' ').filter(!_.startsWith("#")).toList
  }

  val link = Link("link")
  val doc = Page("some returned page #somelink ok")
  val g = RunnableGraph.fromGraph(GraphDSL.create() { implicit b =>
    import GraphDSL.Implicits._

    val in: Source[Link, NotUsed] = Source.single(link)
    val out1 = Sink.foreach[Link](x => println("link: " + x.v))
    val out2 = Sink.foreach[Data](x => println("data: " + x.v))

    val download = Flow.fromFunction[Link, Page] { link => doc }

    val bcast = b.add(Broadcast[Page](2))
    val parseLinks = Flow
      .fromFunction(getLinks)
      .mapConcat(identity)
      .map(x => Link(x))
    val parseData = Flow
      .fromFunction(getData)
      .mapConcat(identity)
      .map(x => Data(x))

    in ~> download ~> bcast ~> parseLinks ~> out1
    bcast ~> parseData ~> out2

    val downloadFlow = Flow[String].map(_ => doc)

    ClosedShape
  })

  g.run()
}
