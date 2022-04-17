package example.sandbox

sealed trait Json
final case class JsObject(get: Map[String, Json]) extends Json
final case class JsString(get: String) extends Json
final case class JsNumber(get: Double) extends Json
final case object JsNull extends Json

//this is type class
trait JsonWriter[A] {
  def write(value: A): Json
}

// packaging in trait, need inheriting
trait IntJsonable {
  implicit val intWriter = new JsonWriter[Int] {
    override def write(value: Int): Json = JsNumber(value.toDouble)
  }
}

// packaging in companion object, always in implicit scope (preffered)
object JsonWriter {
  implicit val doubleWriter = new JsonWriter[Double] {
    override def write(value: Double): Json = JsNumber(value)
  }

  //type class composition, remember to mark parameters as implicit!!!
  implicit def optionWriter[A](implicit writer: JsonWriter[A]): JsonWriter[Option[A]] = new JsonWriter[Option[A]] {
    override def write(option: Option[A]): Json = option match {
      case Some(value) => writer.write(value)
      case None        => JsNull
    }
  }
}

case class Bag(n: Int, f: Float)

//packaging in parameter type, always in implicit scope (preffered)
object Bag {
  implicit val bagWriter = new JsonWriter[Bag] {
    override def write(value: Bag): Json = JsObject(
      Map(
        "n" -> JsNumber(value.n.toDouble),
        "f" -> JsNumber(value.f.toDouble)
      )
    )
  }
}

case class Person(name: String, email: String)

// packaging in regular object, need importing
object JsonWriterInstances {
  implicit val stringWriter = new JsonWriter[String] {
    override def write(value: String): Json = JsString(value)
  }

  implicit val personWriter = new JsonWriter[Person] {
    override def write(value: Person): Json = JsObject(
      Map(
        "name" -> JsString(value.name),
        "email" -> JsString(value.email)
      )
    )
  }
}

object Json {
  def toJson[A](value: A)(implicit w: JsonWriter[A]): Json = w.write(value)
}

object JsonSyntax {
  implicit class JsonWriterOps[A](value: A) {
    def toJson(implicit w: JsonWriter[A]): Json = w.write(value)
  }
}

object JsonMain extends App with IntJsonable {
  import JsonWriterInstances._
  val me = Person("kodus", "kodus8@pm.me")
  val jsonMe = Json.toJson(me)

  import JsonSyntax._
  val jsonMe2 = me.toJson(personWriter)

  val d: Double = 3.14
  val jsonD = Json.toJson(d)

  val n: Int = 69
  val jsonN = Json.toJson(n)

  val bag = Bag(10, 20f)
  val jsonBag = Json.toJson(bag)

  // created using implicit magic™
  val maybeBagHowKnows: Option[Bag] = Some(Bag(1, 2.01f))
  val jsonPossibleBag = Json.toJson(maybeBagHowKnows)
}
