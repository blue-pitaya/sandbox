package example.museum.slick14joins

import slick.jdbc.PostgresProfile.api._

case class ItemRecord(id: Int, name: String, kind: Int)

class Items(tag: Tag) extends Table[ItemRecord](tag, "items") {

  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def name = column[String]("name")
  def kind = column[Int]("kind")

  override def * = (id, name, kind) <> (ItemRecord.tupled, ItemRecord.unapply)
}
