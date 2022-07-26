package example.museum.slick14joins

import slick.jdbc.PostgresProfile.api._

case class HeroRecord(
    id: Int,
    name: String,
    exp: Int,
    strength: Int,
    agility: Int,
    intellect: Int,
    charisma: Int,
    stamina: Int,
    eqRightHand: Option[Int],
    eqLeftHand: Option[Int],
    eqHead: Option[Int],
    eqTorso: Option[Int],
    eqLeftRing: Option[Int],
    eqRightRing: Option[Int],
    eqLegs: Option[Int]
)

class Heroes(tag: Tag) extends Table[HeroRecord](tag, "heroes") {

  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def name = column[String]("name")
  def exp = column[Int]("exp")
  def strength = column[Int]("strength")
  def agility = column[Int]("agility")
  def intellect = column[Int]("intellect")
  def charisma = column[Int]("charisma")
  def stamina = column[Int]("stamina")
  def eqRightHand = column[Option[Int]]("eq_right_hand")
  def eqLeftHand = column[Option[Int]]("eq_left_hand")
  def eqHead = column[Option[Int]]("eq_head")
  def eqTorso = column[Option[Int]]("eq_torso")
  def eqLeftRing = column[Option[Int]]("eq_left_ring")
  def eqRightRing = column[Option[Int]]("eq_right_ring")
  def eqLegs = column[Option[Int]]("eq_legs")

  override def * =
    (
      id,
      name,
      exp,
      strength,
      agility,
      intellect,
      charisma,
      stamina,
      eqRightHand,
      eqLeftHand,
      eqHead,
      eqTorso,
      eqLeftRing,
      eqRightRing,
      eqLegs
    ) <> (HeroRecord.tupled, HeroRecord.unapply)
}
