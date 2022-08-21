package example.experminets.polymorphism

// Polymorphism based on case class pattern matching but with trait composition
//
// Pros:
// very simple
// less case duplication
// more modular
//
// Cons:
// traits are somehow coupled with implemtation
// still there is copy case class duplication

object TraitComposition extends App {
  sealed trait Entity {
    val name: String
  }

  sealed trait Hp extends Entity {
    val hp: Int
  }

  sealed trait AdditionalAttack extends Entity {
    val hit: Int
    val additionalHit: Int
  }

  sealed trait NormalAttack extends Entity {
    val normalHit: Int
  }

  case class Hero(name: String, hit: Int, additionalHit: Int, hp: Int) extends AdditionalAttack with Hp
  case class Mob(name: String, normalHit: Int, hp: Int) extends NormalAttack with Hp
  //this would force attackDmg function to return Option[Int]
  //case class Healer(name: String, hp: Int) extends Entity

  object EntityOps {
    def attackDmg(entity: Entity): Int = entity match {
      case e: AdditionalAttack => e.hit + e.additionalHit
      case e: NormalAttack     => e.normalHit
    }

    def takeDmg(entity: Entity, dmg: Int): Entity = entity match {
      //same logic, because of copy
      case e: Hero => e.copy(hp = e.hp - dmg)
      case e: Mob  => e.copy(hp = e.hp - dmg)
    }

    def scream(entity: Entity, msg: String): String = s"${entity.name} screams ${msg}!"
  }

  val hero = Hero("Scooby", 20, 5, 69)
  val mob = Mob("Ghost", 5, 420)
  val entities = List[Entity](hero, mob)

  def test(entities: List[Entity]): List[String] = entities.map { e =>
    val dmg = EntityOps.attackDmg(e)
    val afterHit = EntityOps.takeDmg(e, 10)
    val scream = EntityOps.scream(e, "yolo")

    val hpAfterHit = afterHit match {
      case e: Hp => e.hp
    }

    s"I do ${dmg} dmg, when I'm hit i have ${hpAfterHit} hp, and i scream: ${scream}."
  }

  test(entities).foreach(println(_))
}
