package example.museum.slick14joins

import slick.jdbc.PostgresProfile.api._

case class ItemInstanceRecord(id: Int, itemId: Int)

class ItemInstances(tag: Tag) extends Table[ItemInstanceRecord](tag, "item_instances") {

  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def itemId = column[Int]("item_id")

  def item = foreignKey("item_id", itemId, TableQueries.items)(_.id)

  override def * = (id, itemId) <>
    (ItemInstanceRecord.tupled, ItemInstanceRecord.unapply)
}
