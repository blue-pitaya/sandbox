package example.experminets.pseudopolymorphism

import cats.data.NonEmptyList

object PseudoPolyMain extends App {
  sealed trait Animal
  case class Cat(hit: Int) extends Animal
  case class Dog(name: String) extends Animal

  trait Strategy[A] {
    def move: A => String
  }

  val catStrategy: Cat => String = c => s"Cat use claw hit which deals ${c.hit} dmg!"
  val dogStrategy: Dog => String = d => s"Dog just barks hit name ${d.name}!"

  implicit val animalStrategy = new Strategy[Animal] {
    override def move: Animal => String = a =>
      a match {
        case c: Cat => catStrategy(c)
        case d: Dog => dogStrategy(d)
      }
  }

  val animals: NonEmptyList[Animal] = NonEmptyList.of(Cat(9), Dog("mfer"))
  animals.toList.foreach(a => println(a.move)) //???

  //2nd approach

  implicit val imCatStrategy = new Strategy[Cat] {
    override def move: Cat => String = c => s"Cat use claw hit which deals ${c.hit} dmg!"
  }
  implicit val imDogStrategy = new Strategy[Dog] {
    override def move: Dog => String = d => s"Dog just barks hit name ${d.name}!"
  }

  case class StrategyImpl[A](a: A, strategy: Strategy[A]) {
    def move: String = strategy.move(a)
  }

  implicit def asStrategy[A](a: A)(implicit s: Strategy[A]): StrategyImpl[A] =
    StrategyImpl(a, s)

  val animals2: NonEmptyList[StrategyImpl[_]] = NonEmptyList.of(asStrategy(Cat(3)), asStrategy(Dog("coin")))
  animals2.toList.foreach(a => println(a.move))

  //3rd approach could be HList
}
