package example.experminets.formvalidation

import scala.collection.immutable

case class User(login: String, password: String, age: Int)
//want to restrics login (3-32 chars inc only letters, pasword 6-256 chars alfanumeric, age min 18)

trait Rule2[A] extends (A => Boolean) {
  def errorMsg: String
}

object Main {
  val minCharRule: Int => Rule2[String] = n =>
    new Rule2[String] {
      override def apply(str: String): Boolean = str.length() >= n
      override def errorMsg: String = s"length must be > $n"
    }
  val lettersOnlyRule: Rule2[String] = new Rule2[String] {
    override def apply(v1: String): Boolean = "^[a-zA-Z]+$".r.matches(v1)
    override def errorMsg: String = "value must be only letters"
  }

  def validateField[A](value: A, rules: List[Rule2[A]]): List[String] =
    rules.flatMap(r => if (r(value)) List() else List(r.errorMsg))

  def putErrors[A](fieldName: String, errors: List[String]): Map[String, List[String]] = errors match {
    case immutable.Nil => Map()
    case errors        => Map(fieldName -> errors)
  }

  def validateUser(user: User): Map[String, List[String]] = {
    val loginRules = List(minCharRule(3), lettersOnlyRule)
    val passwordRules = List[Rule2[String]]()
    val ageRules = List[Rule2[Int]]()

    putErrors("login", validateField(user.login, loginRules)) ++
    putErrors("password", validateField(user.password, passwordRules)) ++
    putErrors("age", validateField(user.age, ageRules))
  }
}
