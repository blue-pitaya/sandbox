package example.experminets.onlymultipletraits

object FartMain extends App {
  //val modules = new FunkyComponentImpl //this wont compile
  val modules =
    new FunkyComponentImpl with NumberComponentImpl //this will, why not (Funky and Number dont depends on each other)
  val modules2 = new FartComponentImpl with NumberComponentImpl //this too (but FartComp depends on NumberComp)
}
