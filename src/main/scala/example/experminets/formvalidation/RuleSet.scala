//inspiration: https://gist.github.com/melvic-ybanez/e730fc642b179f7494621d17175e3ed1
package example.experminets.formvalidation

import cats.{Monoid, Semigroup}
import cats.implicits._

trait Rule[A] extends (A => Boolean)

trait Validatable[A] extends (A => Boolean)

//TODO: make it even better!

object RuleSet {
  implicit def ruleMonoid[A: Semigroup] = new Monoid[Rule[A]] {
    override def combine(x: Rule[A], y: Rule[A]): Rule[A] = n => x(n) && y(n)
    override def empty: Rule[A] = _ => true
  }

  implicit class RuleCombineOperator[A: Semigroup](r: Rule[A]) {
    def &(other: Rule[A]): Rule[A] = Monoid.combine(r, other)
  }

  val minChars: Int => Rule[String] = n => str => str.length() >= n
  val maxChars: Int => Rule[String] = n => str => str.length() <= n
  val charsBetweenInc: (Int, Int) => Rule[String] = (min, max) => minChars(min) & maxChars(max)
  val onlyNumbers: Rule[String] = "[0-9]+".r.matches(_)
  val startsWithOddDigit: Rule[String] = "^[1|3|5|7|9]".r.matches(_)

  def validateField[A](field: A, rule: Rule[A]): Boolean = rule(field)
}

case class Person(name: String, age: Int)

object Person {
  import RuleSet._
  implicit val personValidatable = new Validatable[Person] {
    override def apply(p: Person): Boolean =
      validateField(p.name, charsBetweenInc(1, 32))
  }
}
