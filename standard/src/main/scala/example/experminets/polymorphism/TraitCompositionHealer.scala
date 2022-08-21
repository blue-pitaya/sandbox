package example.experminets.polymorphism

//This is example of trait composition with Entity that dont attack, only is healing

object TraitCompositionHealer extends App {
  sealed trait Entity {
    val name: String
  }

  sealed trait Hp extends Entity {
    val hp: Int
  }

  sealed trait Attack

  sealed trait AdditionalAttack extends Attack {
    val hit: Int
    val additionalHit: Int
  }

  sealed trait NormalAttack extends Attack {
    val normalHit: Int
  }

  case class Hero(name: String, hit: Int, additionalHit: Int, hp: Int) extends AdditionalAttack with Hp
  case class Mob(name: String, normalHit: Int, hp: Int) extends NormalAttack with Hp
  case class Healer(name: String, hp: Int) extends Hp

  object EntityOps {
    // Option[Int] can become Int if entity is replace with Attack, and then solving what can attack or not is moved closer to usage
    def attackDmg(entity: Entity): Option[Int] = entity match {
      case e: AdditionalAttack => Some(e.hit + e.additionalHit)
      case e: NormalAttack     => Some(e.normalHit)
      case _                   => None
    }

    def takeDmg(entity: Entity, dmg: Int): Entity = entity match {
      //same logic, because of copy
      case e: Hero   => e.copy(hp = e.hp - dmg)
      case e: Mob    => e.copy(hp = e.hp - dmg)
      case e: Healer => e.copy(hp = e.hp - dmg)
    }

    def scream(entity: Entity, msg: String): String = s"${entity.name} screams ${msg}!"

    def healAmount(entity: Entity): Option[Int] = entity match {
      case Healer(name, hp) => Some(42)
      case _                => None
    }
  }

  val hero = Hero("Scooby", 20, 5, 69)
  val mob = Mob("Ghost", 5, 420)
  val healer = Healer("Zombie", 999)
  val entities = List[Entity](hero, mob, healer)

  def test(entities: List[Entity]): List[String] = entities.map { e =>
    val dmg = EntityOps.attackDmg(e)
    val afterHit = EntityOps.takeDmg(e, 10)
    val scream = EntityOps.scream(e, "yolo")
    val heal = EntityOps.healAmount(e)

    val hpAfterHit = afterHit match {
      case e: Hp => e.hp
    }

    s"I do ${dmg} dmg, when I'm hit i have ${hpAfterHit} hp and i heal ${heal}, and i scream: ${scream}."
  }

  test(entities).foreach(println(_))
}
