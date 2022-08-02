package example.experminets.polymorphism

// Basic polymorphism based on case class pattern matching
//
// Pros:
// very simple
// addind new Entity will throw warning about not exhaustive pattern matching
//
// Cons:
// code duplication when 2 entities have same logic for single function

object BasicPatternMatching extends App {
  sealed trait Entity

  case class Hero(name: String, hit: Int, additionalHit: Int, hp: Int) extends Entity
  case class Mob(name: String, hit: Int, hp: Int) extends Entity

  object EntityOps {
    def attackDmg(entity: Entity): Int = entity match {
      case Hero(_, hit, additionalHit, _) =>
        hit + additionalHit
      case Mob(_, hit, _) => hit
    }

    def takeDmg(entity: Entity, dmg: Int): Entity = entity match {
      //same logic!
      case e: Hero => e.copy(hp = e.hp - dmg)
      case e: Mob  => e.copy(hp = e.hp - dmg)
    }

    //same logic can be moved to single function
    private def handleScream(name: String, msg: String): String = s"${name} screams ${msg}!"
    def scream(entity: Entity, msg: String): String = entity match {
      case e: Hero => handleScream(e.name, msg)
      case e: Mob  => handleScream(e.name, msg)
    }
  }

  val hero = Hero("Scooby", 20, 5, 69)
  val mob = Mob("Ghost", 5, 420)
  val entities = List[Entity](hero, mob)

  def test(entities: List[Entity]): List[String] = entities.map { e =>
    val dmg = EntityOps.attackDmg(e)
    val afterHit = EntityOps.takeDmg(e, 10)
    val scream = EntityOps.scream(e, "yolo")

    val hpAfterHit = afterHit match {
      case e: Hero => e.hp
      case e: Mob  => e.hp
    }

    s"I do ${dmg} dmg, when I'm hit i have ${hpAfterHit} hp, and i scream: ${scream}."
  }

  test(entities).foreach(println(_))
}
