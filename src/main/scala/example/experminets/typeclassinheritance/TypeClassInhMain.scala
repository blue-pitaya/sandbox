package example.experminets.typeclassinheritance

import scala.util.Random

sealed trait Entity

trait RandomAttack extends Entity {
  val hitMin: Int
  val hitMax: Int
}

trait StaticAttack extends Entity {
  val hit: Int
}

trait Health extends Entity {
  val hp: Int
}

case class Hero() extends RandomAttack with Health {
  override val hitMin: Int = 10
  override val hitMax: Int = 20
  override val hp: Int = 50
}

case class Mob() extends StaticAttack with Health {
  override val hit: Int = 12
  override val hp: Int = 40
}

case class NoAttackMob() extends Health {
  override val hp: Int = 99
}

trait Attack[A <: Entity] {
  def getDmg(a: A): Int
}

object AttackOps {
  def getDmg[A <: Entity](a: A)(implicit attack: Attack[A]): Int = attack.getDmg(a)
}

object PierogiOps {
  def attack: PartialFunction[Entity, Int] = {
    case a: RandomAttack =>
      Random.nextInt(a.hitMax - a.hitMin) + a.hitMin + 1
    case a: StaticAttack =>
      a.hit
  }
}

object TypeClassInhMain extends App {
  val entities: List[Entity] = List(Hero(), Mob(), NoAttackMob())

  implicit val randomAttackImpl = new Attack[RandomAttack] {
    override def getDmg(a: RandomAttack): Int = Random.nextInt(a.hitMax - a.hitMin) + a.hitMin + 1
  }

  implicit val staticAttackImpl = new Attack[StaticAttack] {
    override def getDmg(a: StaticAttack): Int = a.hit
  }

  implicit def entityAttackImpl[A <: Entity](implicit attack: Attack[A]) = attack

  //this wont work
  //entities.foreach(e => println(AttackOps.getDmg(e)))

  //this will throw exception
  val dmgs = entities.map(e => PierogiOps.attack(e))
}
