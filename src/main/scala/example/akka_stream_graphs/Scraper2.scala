package example.akka_stream_graphs

import akka.stream.ClosedShape
import akka.stream.scaladsl._
import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors

// TODO: source with flow back to flow1 xd
object Scraper2 extends App {
  implicit val system = ActorSystem(Behaviors.empty, "system")

  def getLinks(page: Page): List[String] = {
    page.v.split(' ').filter(_.startsWith("#")).toList
  }

  def getData(page: Page): List[String] = {
    page.v.split(' ').filter(!_.startsWith("#")).toList
  }

  def docMatcher(link: Link): Page = link match {
    case Link("page1") => Page("ok #page2 yes #page3")
    case Link("page2") => Page("xd lol")
    case Link("page3") => Page("elo #page4 nono")
    case Link("page4") => Page("end")
    case _             => Page("")
  }

  val g = RunnableGraph.fromGraph(GraphDSL.create() { implicit b =>
    import GraphDSL.Implicits._

    val in = Source.single[Link](Link("page1"))

    val download = Flow.fromFunction[Link, Page](docMatcher)
    val bcast = b.add(Broadcast[Page](2))
    val parseLinks = Flow
      .fromFunction(getLinks)
      .mapConcat(identity)
      .map(x => Link(x))
    val parseData = Flow
      .fromFunction(getData)
      .mapConcat(identity)
      .map(x => Data(x))

    ClosedShape
  })
}
