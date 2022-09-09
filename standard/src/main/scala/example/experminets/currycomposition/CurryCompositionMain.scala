package example.experminets.currycomposition

object CurryCompositionMain extends App {
  case class Cat(name: String, age: Int) {
    def changeMyName(name: String): Cat = copy(name = name)

    def changeMyAge(age: Int): Cat = copy(age = age)
  }

  def changeName(name: String)(cat: Cat): Cat = cat.copy(name = name)

  def changeAge(age: Int)(cat: Cat): Cat = cat.copy(age = age)

  def changeNameAndAge(name: String, age: Int)(cat: Cat): Cat = (changeName(name) _ andThen changeAge(age))(cat)

  def changeNameAndAge2: (String, Int) => Cat => Cat = (name, age) => (changeName(name) _ andThen changeAge(age))

  val kitten = Cat(name = "Big daddy", age = 997)

  println(changeNameAndAge("Sugar daddy", 420)(kitten))
  println(changeNameAndAge2("Sugar daddy", 420)(kitten))
  println(kitten.changeMyName("Sugar daddy").changeMyAge(420))
}
